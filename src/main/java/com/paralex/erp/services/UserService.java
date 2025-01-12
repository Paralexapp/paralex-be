package com.paralex.erp.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.paralex.erp.annotations.ValidateUpdateItem;
import com.paralex.erp.commons.utils.EmailContent;
import com.paralex.erp.commons.utils.Helper;
import com.paralex.erp.configs.JwtService;
import com.paralex.erp.dtos.*;
import com.paralex.erp.entities.NewWallet;
import com.paralex.erp.entities.Otp;
import com.paralex.erp.entities.Token;
import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.enums.RegistrationLevel;
import com.paralex.erp.enums.UserType;
import com.paralex.erp.exceptions.*;
import com.paralex.erp.repositories.OtpRepository;
import com.paralex.erp.repositories.ResetRequestRepo;
import com.paralex.erp.repositories.TokenRepo;
import com.paralex.erp.repositories.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import okhttp3.HttpUrl;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RequiredArgsConstructor
@Validated
@Service
@Log4j2
public class UserService {
    public static final int MAX_CODE_LENGTH = 6;
    public static final String REDIRECT_URL_KEY = "redirectUrl";
    public static final String PURPOSE_MUSTACHE_KEY = "purpose";
    public static final String EEEE_MMMM_DD_YYYY_HH_MM_SS = "EEEE, MMMM dd, yyyy HH:mm:ss";
    public static final String EMAIL_VERIFICATION = "Email Verification";
    public static final String DATE_OF_ACTION = "dateOfAction";
    private static final String OTP_CODE_KEY = "code";
    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final EntityManager entityManager;
    private final FirebaseService firebaseService;
    private final EmailService emailService;
    private final PaymentGatewayService paymentGatewayService;
    private final WalletService walletService;
    private final Helper helper;
    private final OtpService otpService;
    private final TokenRepo tokenRepo;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final ResetRequestRepo resetRequestRepo;
    private final Cloudinary cloudinary;

    @Value("${app.cookie.domain}")
    private String domain;

    @Value("${server.url}")
    private String hostUrl;

    @Value("${mail.from.address}")
    private String fromAddress;

    @Value("${email.template.password-reset}")
    private String passwordResetTemplate;

    public void sendEmailVerification(@NotNull GenerateEmailVerificationEmailDto emailVerificationDto) throws IOException, FirebaseAuthException, MessagingException {
        final String idToken = emailVerificationDto.getIdToken();
        final FirebaseToken firebaseToken = firebaseService.verifyIdToken(idToken, true);

        if (firebaseToken.isEmailVerified())
            return;

//        sendEmailVerificationMail(firebaseToken.getEmail(), idToken);
    }

    public void sendEmailVerificationMail(String toAddress, String idToken, String destination) throws IOException, FirebaseAuthException, MessagingException {
        final String subject = "Email Verification";
        final String emailBody = prepareEmailVerificationEmail(toAddress, idToken, destination);

        emailService.sendEmail(EmailEnvelopeDto.builder()
                .emailBody(emailBody)
                .fromAddress(fromAddress)
                .toAddress(toAddress)
                .subject(subject)
                .build());
    }

    public void sendPasswordReset(@NotNull GeneratePasswordResetEmailDto passwordResetEmailDto) throws IOException, FirebaseAuthException, MessagingException {
        final String email = passwordResetEmailDto.getEmail();
//        final String destination = passwordResetEmailDto.getRedirectUrl();

//        sendPasswordResetMail(email, destination);
    }

    public void sendPasswordResetMail(@NonNull @NotEmpty @NotBlank String toAddress) throws IOException, FirebaseAuthException, MessagingException {
        final String subject = "Password Reset";
        final String emailBody = preparePasswordResetEmail(toAddress);

        log.info("prepared password reset template");

        emailService.sendEmail(EmailEnvelopeDto.builder()
                .emailBody(emailBody)
                .fromAddress(fromAddress)
                .toAddress(toAddress)
                .subject(subject)
                .build());
    }

    public String preparePasswordResetEmail(@NonNull @NotEmpty @NotBlank String email) throws IOException, FirebaseAuthException {
        final HashMap<String, Object> scopes = new HashMap<>();
        final String code = RandomStringUtils.randomAlphanumeric(MAX_CODE_LENGTH);

        final String purpose = "Password Reset";
        String link = firebaseService.generatePasswordResetLink(email);
        final HttpUrl parsedLink = HttpUrl.parse(link);
        assert parsedLink != null;
        final String MODE_KEY = "mode";
        final String OOB_CODE_KEY = "oobCode";
        final String mode = parsedLink.queryParameter(MODE_KEY);
        final String oobCode = parsedLink.queryParameter(OOB_CODE_KEY);
        final String LINK_MUSTACHE_KEY = "link";

        link = UriComponentsBuilder.newInstance()
                .scheme(parsedLink.scheme())
                .host(parsedLink.host())
                .port(parsedLink.port())
                .pathSegment("get-started", "change", "password")
                .queryParam(MODE_KEY, mode)
                .queryParam(OOB_CODE_KEY, oobCode)
                .queryParam(OTP_CODE_KEY, encodeAsBase64(code))
                .build()
                .toString();

        log.debug("[link] : " + link);

        scopes.put(PURPOSE_MUSTACHE_KEY, purpose);
        scopes.put(OTP_CODE_KEY, code);
        scopes.put(LINK_MUSTACHE_KEY, link);
        scopes.put(DATE_OF_ACTION, LocalDateTime.now().format(DateTimeFormatter.ofPattern(EEEE_MMMM_DD_YYYY_HH_MM_SS)));

        return emailService.prepareTemplate(emailService.loadMustacheTemplate(passwordResetTemplate).getInputStream(),
                passwordResetTemplate.split("..")[0], scopes);
    }

    public String prepareEmailVerificationEmail(@NonNull @NotEmpty @NotBlank String email, @NotNull @NotBlank @NotEmpty String idToken, @NonNull @NotEmpty @NotBlank String destination) throws IOException, FirebaseAuthException {
        final HashMap<String, Object> scopes = new HashMap<>();
        final String code = RandomStringUtils.randomAlphanumeric(MAX_CODE_LENGTH);

        final String purpose = "Email Verification";
        String link = firebaseService.generateEmailVerificationLink(email);
        final HttpUrl parsedLink = HttpUrl.parse(link);
        assert parsedLink != null;
        final String MODE_KEY = "mode";
        final String OOB_CODE_KEY = "oobCode";
        final String mode = parsedLink.queryParameter(MODE_KEY);
        final String oobCode = parsedLink.queryParameter(OOB_CODE_KEY);
        final String OTP_CODE_KEY = "code";
        final String PURPOSE_MUSTACHE_KEY = "purpose";
        final String LINK_MUSTACHE_KEY = "link";

        link = UriComponentsBuilder.newInstance()
                .scheme(parsedLink.scheme())
                .host(parsedLink.host())
                .port(parsedLink.port())
                .pathSegment("confirm", "email", encodeAsBase64(idToken))
                .queryParam(MODE_KEY, mode)
                .queryParam(OOB_CODE_KEY, oobCode)
                .queryParam(OTP_CODE_KEY, encodeAsBase64(code))
                .queryParam(REDIRECT_URL_KEY, destination)
                .build()
                .toString();

        log.debug("[link] : " + link);

        scopes.put(PURPOSE_MUSTACHE_KEY, purpose);
        scopes.put(OTP_CODE_KEY, code);
        scopes.put(LINK_MUSTACHE_KEY, link);
        scopes.put("dateOfAction", LocalDateTime.now().format(DateTimeFormatter.ofPattern(EEEE_MMMM_DD_YYYY_HH_MM_SS)));

        return emailService.prepareTemplate(emailService.loadMustacheTemplate(passwordResetTemplate).getInputStream(),
                passwordResetTemplate.split("..")[0], scopes);
    }

    public String encodeAsBase64(@NonNull @NotEmpty @NotBlank String idToken) {
        return Base64.getEncoder().encodeToString(idToken.getBytes());
    }

    public ResponseCookie getDestroyedCookie() {
        return ResponseCookie.from("session", null)
                .domain(domain)
                .path("/")
                .sameSite("Strict")
                .httpOnly(true)
                .secure(true)
                .maxAge(Duration.ofDays(0))
                .build();
    }

    public ResponseCookie getSignInCookie(String idToken) {
        return ResponseCookie.from("session", idToken)
                .domain(domain)
                .path("/")
                .sameSite("Strict")
                .httpOnly(true)
                .secure(true)
                .maxAge(Duration.ofDays(1))
                .build();
    }

    public FirebaseToken verifyIdToken(String idToken, Boolean revoked) throws IOException, FirebaseAuthException {
        return firebaseService.getAuth()
                .verifyIdToken(idToken, Objects.requireNonNullElse(revoked, true));
    }

    public Long countAllOrBetweenTime(CountDto countDto) {
        final LocalDateTime startDate = countDto.getStartDate();
        final LocalDateTime endDate = countDto.getEndDate();
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        final Root<UserEntity> root = criteriaQuery.from(UserEntity.class);
        final List<Predicate> predicateList = new ArrayList<>();

        criteriaQuery.select(criteriaBuilder.count(root));

        if (startDate != null && endDate != null)
            predicateList.add(criteriaBuilder.between(root.get("time"), startDate, endDate));

        return entityManager
                .createQuery(criteriaQuery.where(predicateList.toArray(new Predicate[0])))
                .getSingleResult();
    }

    // INFO No Need for AuthZ here
    public UserEntity findUserBy(FindUserProfileDto findUserProfileDto) {
        var id = findUserProfileDto.getId();
        var email = findUserProfileDto.getEmail();
        var phoneNumber = findUserProfileDto.getPhoneNumber();
        var entity = UserEntity.builder()
                .id(id)
                .email(email)
                .phoneNumber(phoneNumber)
                .build();

        var example = Example.of(entity, ExampleMatcher.matchingAll()
                .withIgnoreNullValues()
                .withIgnorePaths("id", "time", "photoUrl"));

        return userRepository.findOne(example).orElse(null);
    }

    @Transactional
    public void updateUserDetails(
            @NotNull @NotEmpty @NotBlank String id,
            @ValidateUpdateItem(
                    keyList = { "id", "time" },
                    modelClass = FindUserProfileDto.class) List<UpdateItemDto> changes) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaUpdate<UserEntity> update = criteriaBuilder.createCriteriaUpdate(UserEntity.class);
        final Root<UserEntity> root = update.from(UserEntity.class);

        changes.forEach(change -> update.set(change.getKey(), change.getValue()));
        update.where(criteriaBuilder.equal(root.get("id"), id));

        entityManager.createQuery(update).executeUpdate();
    }



    public UserEntity verifyUserProfile(CreateUserProfileDto createUserProfileDto) throws FirebaseAuthException, IOException {
        var idToken = createUserProfileDto.getIdToken();
        var firebaseToken = verifyIdToken(idToken, true);

        return findUserByEmail(firebaseToken.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Unrecognized Entity"));
    }

    public String createUserProfile(@NotNull CreateProfileForUserDto createProfileForUserDto) throws IOException, FirebaseAuthException {
        final var email = createProfileForUserDto.getEmail();
        final var phoneNumber = createProfileForUserDto.getPhoneNumber();
        UserRecord userRecord = null;
        final var existingUserRecord = firebaseService.getUserFromFirebaseBy(email, phoneNumber);

        if (existingUserRecord.isEmpty()) {
            final var createUserRequest = getCreateUserRequest(createProfileForUserDto, email, phoneNumber);

            userRecord = firebaseService.createUser(createUserRequest);
        } else {
            userRecord = existingUserRecord.get();
        }

        final var userByEmail = findUserByEmail(userRecord.getEmail());
        final var userByPhoneNumber = findUserByPhoneNumber(userRecord.getPhoneNumber());

        if (userByEmail.isEmpty() && userByPhoneNumber.isEmpty()) {
            return createUserProfile(userRecord);
        }

        return userByEmail.orElse(userByPhoneNumber.orElseThrow())
                .getId();
    }

    @NotNull
    private static UserRecord.CreateRequest getCreateUserRequest(CreateProfileForUserDto createProfileForUserDto, String email, String phoneNumber) {
        final var createUserRequest = new UserRecord.CreateRequest();

        createUserRequest.setEmail(email);
        createUserRequest.setEmailVerified(false);
        createUserRequest.setDisplayName(createProfileForUserDto.getFirstName() + " " + createProfileForUserDto.getLastName());
        createUserRequest.setPhoneNumber(phoneNumber);
        createUserRequest.setPassword(createProfileForUserDto.getPassword());
        createUserRequest.setPhotoUrl(createProfileForUserDto.getPhotoUrl());
        createUserRequest.setDisabled(false);
        return createUserRequest;
    }

    public String createUserProfile(CreateUserProfileDto createUserProfileDto) throws FirebaseAuthException, IOException {
        var idToken = createUserProfileDto.getIdToken();
        var firebaseAuth = firebaseService.getAuth();
        var firebaseToken = firebaseAuth.verifyIdToken(idToken, true);
        var userRecord = firebaseAuth.getUser(firebaseToken.getUid());

        return createUserProfile(userRecord);
    }

    public String createUserProfile(@NotNull UserRecord userRecord) {
        var userEntity = UserEntity.builder()
                .name(userRecord.getDisplayName())
                .email(userRecord.getEmail())
                .phoneNumber(userRecord.getPhoneNumber())
                .photoUrl(userRecord.getPhotoUrl())
                .build();

        return userRepository.save(userEntity).getId();
    }

    public Optional<UserEntity> findUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    public Optional<UserEntity> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public GlobalResponse<?>  register(RegistrationDto dto) {

        try {
            Optional<UserEntity> exist = userRepository.findByEmail(dto.getEmail());

            if(exist.isPresent()){
                throw new ErrorException("Customer Already Exist");
            }

            if(!dto.getPassword().equals(dto.getConfirmPassword())){
                throw new ErrorException("Passwords do not match");
            }
            if(!helper.isPasswordValidInLengthAndInFormat(dto.getPassword())){
                throw new ErrorException("Password must be between 8 and 20 characters long and " +
                        "contain at least one uppercase letter, one lowercase letter, and one digit.");
            }

            UserEntity customer = new UserEntity();
            customer.setPassword(helper.encodePassword(dto.getPassword()));
            customer.setTime(LocalDateTime.now());
            customer.setRegistrationLevel(RegistrationLevel.VALIDATE_OTP);
            customer.setEmail(dto.getEmail());

            switch (dto.getUserType().toUpperCase()){
                case "USER":{
                    customer.setUserType(UserType.USER);
                }
                case "SERVICE_PROVIDER_LAWYER":{
                    customer.setUserType(UserType.SERVICE_PROVIDER_LAWYER);
                    break;
                }
                case "SERVICE_PROVIDER_RIDER":{
                    customer.setUserType(UserType.SERVICE_PROVIDER_RIDER);
                    break;
                }

                default:{
                    throw new ErrorException("Invalid user type");
                }
            }

            userRepository.save(customer);

            String otp_digit = otpService.generateOtp();

            LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);

            Otp otp = new Otp();
            otp.setCustomerId(customer.getId());
            otp.setEmail(dto.getEmail());
            otp.setOtp(otp_digit);
            System.out.println(otp);
            otpRepository.save(otp);
            String message = EmailContent.verificationEmail(otp_digit);
             EmailDto emailDto = EmailDto.builder()
                     .recipient(dto.getEmail())
                    .subject("Account Verification")
                     .messageBody(message)
                    .build();
            emailService.sendOtpEmail(emailDto);

            GlobalResponse<String> response = new GlobalResponse<>();
            response.setStatus(HttpStatus.ACCEPTED);
            response.setMessage("Registration successful. Please check your email or phone for OTP to verify your Account");
            return response;

        }
        catch (Exception e){
            throw new ErrorException(e.getMessage());
        }

    }

    public GlobalResponse<?>  validateOtp(ValidateDto validateDto) {
        try {
            LocalDateTime currentTime = LocalDateTime.now();
            String otpValue = validateDto.getOtp();
            Optional<Otp> otp = otpRepository.findByOtp(otpValue);

            otp.ifPresentOrElse(
                    otpEntity -> {

                        UserEntity customer = userRepository.findById(otp.get().getCustomerId()).orElseThrow();
                        customer.setEnabled(true);
                        customer.setRegistrationLevel(RegistrationLevel.PROFILE_UPDATE);
                        userRepository.save(customer);
                        otpRepository.deleteById(String.valueOf(otpEntity.getId()));
                    },
                    () -> {
                        throw new RuntimeException("Invalid verification code");
                    }
            );
            GlobalResponse<String> response = new GlobalResponse<>();
            response.setStatus(HttpStatus.ACCEPTED);
            response.setMessage("Valid OTP");
            return response;
        } catch (Exception e) {
            GlobalResponse<String> response = new GlobalResponse<>();
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setDebugMessage(e.getMessage());
            response.setMessage("Error validating OTP");
            return response;
        }
    }

    public GlobalResponse<?> login(LoginDTO loginDTO) throws Exception {

        Optional<UserEntity> customer = userRepository.findByEmail(loginDTO.getEmail()) ;

        if(!helper.isEmailValid(loginDTO.getEmail())){
            throw new EmailNotValidException("Invalid email address");
        }
        if (customer.isEmpty()) {
            throw new UsernameNotFoundException( "Wrong username or password!");

        }

        if (!helper.isPasswordCorrect(loginDTO.getPassword(), customer.get().getPassword())){
            throw new IncorrectDetailsException("Wrong username or password!");
        }

        if (!customer.get().isEnabled()) {
            throw new UsernameNotFoundException( "Account is yet to be verified. Kindly confirm your email!");}

        else {
            GlobalResponse<String> response = new GlobalResponse<>();
            response.setStatus(HttpStatus.ACCEPTED);
            response.setMessage("Login successfully");
            response.setData(jwtService.generateToken(customer.get()));
            return response;
        }
    }


    public GlobalResponse<?>  logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails){
            String email = userDetails.getUsername();
            Optional<UserEntity> customer = userRepository.findByEmail(email);
            customer.ifPresent(this::revokeAllToken);
        }
        SecurityContextHolder.clearContext();
        GlobalResponse<String> response = new GlobalResponse<>();
        response.setStatus(HttpStatus.ACCEPTED);
        response.setMessage("logged out successfully");
        return response;
    }


    public GlobalResponse<?>  updateProfile(UpdateProfileDto updateProfileDto) throws Exception {
        UserEntity customer = userRepository.findByEmail(updateProfileDto.getEmail()).orElseThrow(()->new ErrorException("Account not found"));
        customer.setFirstName(updateProfileDto.getFirstName());
        customer.setLastName(updateProfileDto.getLastName());
        customer.setPhoneNumber(updateProfileDto.getPhoneNumber());
        customer.setDateOfBirth(updateProfileDto.getDateOfBirth());
        customer.setName(updateProfileDto.getFirstName() + " " + updateProfileDto.getLastName());
        customer.setRegistrationLevel(RegistrationLevel.KYC_COMPLETED);

        final var customerCode = paymentGatewayService.createPaystackCustomer(CreateCustomerDto.builder()
                .firstName(customer.getName().split(" ")[0])
                .lastName(customer.getName().split(" ")[1])
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .build());

        customer.setCustomerCode(customerCode);
        userRepository.save(customer);

        String businessId = UUID.randomUUID().toString();

        CreateWalletDTO createWalletDTO =  new CreateWalletDTO();
        createWalletDTO.setName(customer.getFirstName() + " " + customer.getLastName());
        createWalletDTO.setBusinessId(businessId);
        createWalletDTO.setPhoneNumber(customer.getPhoneNumber());
        createWalletDTO.setEmail(customer.getEmail());
        createWalletDTO.setEmployeeId("business");
        createWalletDTO.setAccountType("business");

        // Call createWallet and extract the walletId
        Object walletResponse = walletService.createWallet(createWalletDTO);

        if (walletResponse instanceof OkResponse) {
            OkResponse<NewWallet> okResponse = (OkResponse<NewWallet>) walletResponse;
            NewWallet savedWallet = okResponse.getData();
            String walletId = savedWallet.getWalletId();
            String savedWalletBusinessId = savedWallet.getBusinessId();

            // Save the walletId to the customer entity
            customer.setWalletId(walletId);
            customer.setBusinessId(savedWalletBusinessId);
            userRepository.save(customer);
        } else if (walletResponse instanceof FailedResponse) {
            FailedResponse failedResponse = (FailedResponse) walletResponse;
            throw new ErrorException("Wallet creation failed: " + failedResponse.getDebugMessage());
        }

//        walletService.createWallet(createWalletDTO);
        GlobalResponse<String> response = new GlobalResponse<>();
        response.setStatus(HttpStatus.ACCEPTED);
        response.setMessage("User Profile Updated saved successfully");
        return response;
    }


    public Optional<UserEntity> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public CustomerResponseDTO getUserById(String id) throws Exception {
        UserEntity customer = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        return modelMapper.map(customer, CustomerResponseDTO.class);
    }

    public CustomerResponseDTO getLoggedInUser(HttpServletRequest request) throws Exception {
        UserEntity customer  = helper.extractLoggedInCustomer(request);
        return modelMapper.map(customer, CustomerResponseDTO.class);
    }

    public String getRegistrationLevel(HttpServletRequest request) {
        UserEntity customer = helper.extractLoggedInCustomer(request);
        return customer.getRegistrationLevel().toString();
    }

    public GlobalResponse<?> initiatePasswordRequest(String email) {
        Optional<UserEntity> customer = userRepository.findByEmail(email);

        if (!helper.isEmailValid(email)) {
            throw new EmailNotValidException("Invalid email address");
        }

        String otp = otpService.generateOtp();

        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);
        Otp o_tp = new Otp();
        o_tp.setCustomerId(customer.get().getId());
        o_tp.setEmail(customer.get().getEmail());
        o_tp.setOtp(otp);
        otpRepository.save(o_tp);

        String resetToken = "";

        if (customer.isPresent()) {
            ResetRequest request = new ResetRequest();
            resetToken = UUID.randomUUID().toString();
            LocalDateTime restTokenExpiry = LocalDateTime.now().plusMinutes(10);

            request.setResetToken(resetToken);
            request.setResetTokenExpiry(restTokenExpiry);
            request.setCustomerId(customer.get().getId());

            resetRequestRepo.save(request);

//            String resetPasswordLink = hostUrl + "api/v1/auth/reset-password?token=" + resetToken;
            String message = EmailContent.forgotPasswordEmail(customer.get().getFirstName(), otp);
            EmailDto emailDto = EmailDto.builder()
                    .recipient(customer.get().getEmail())
                    .subject("Reset Password Notification")
                    .messageBody(message)
                    .build();

            emailService.sendOtpEmail(emailDto);
        } else {
            // Handle case where customer is not found
            return new GlobalResponse<>(HttpStatus.NOT_FOUND, "Customer not found");
        }

        GlobalResponse<String> response = new GlobalResponse<>();
        response.setStatus(HttpStatus.ACCEPTED);
        response.setToken(resetToken);
        response.setMessage("Instructions on how to reset your password have been sent to your email. Token: " + resetToken);
        return response;
    }



    public GlobalResponse<?>  forgotPassword(ForgotPasswordDto forgotPasswordDto) throws BadRequestException {
        Optional<ResetRequest> resetRequestOptional = resetRequestRepo.findByResetToken(forgotPasswordDto.getResetToken());
        if (resetRequestOptional.isPresent()) {
            ResetRequest resetRequest = resetRequestOptional.get();
            if (resetRequest.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
                throw new TokenExpiredException("Token already expired");
            }
            Optional<UserEntity> customerOptional = userRepository.findById(resetRequest.getCustomerId());
            if(customerOptional.isPresent()){
                UserEntity customer = customerOptional.get();
                if(!Objects.equals(forgotPasswordDto.getNewPassword(), forgotPasswordDto.getConfirmPassword())){
                    throw new BadRequestException("Password does not match");
                }
                if(!helper.isPasswordValidInLengthAndInFormat(forgotPasswordDto.getNewPassword())){
                    throw new BadRequestException("Invalid password format");
                }
                customer.setPassword(helper.encodePassword(forgotPasswordDto.getNewPassword()));
                userRepository.save(customer);

                resetRequest.setResetToken(null);
                resetRequest.setResetTokenExpiry(null);
                resetRequestRepo.save(resetRequest);

                GlobalResponse<String> response = new GlobalResponse<>();
                response.setStatus(HttpStatus.ACCEPTED);
                response.setMessage("Password successfully updated");
                return response;
            }
        }
        return null;
    }

    public String uploadGeneralFile(MultipartFile multipartFile) throws IOException {
        return cloudinary.uploader()
                .upload(multipartFile.getBytes(),
                        Map.of("public_id", "_"+UUID.randomUUID()))
                .get("url")
                .toString();
    }

    public String uploadAudio_Video(MultipartFile file) throws IOException {
        Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("resource_type", "auto"));
        // Extract and return the secure_url
        return uploadResult.get("secure_url").toString();
    }

    private void revokeAllToken(UserEntity customer) {
        List<Token> tokenList = tokenRepo.findAllByCustomerIdAndIsExpiredAndIsExpired(customer.getId(),false,false);
        if (tokenList.isEmpty()) {
            return;
        }
        for (Token token : tokenList) {
            token.setIsRevoked(true);
            token.setIsExpired(true);
            tokenRepo.saveAll(tokenList);
        }
    }
}



