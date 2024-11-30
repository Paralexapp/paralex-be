package com.paralex.erp.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.paralex.erp.dtos.ErrorResponse;
import com.paralex.erp.dtos.ErrorResponseDto;
import com.paralex.erp.dtos.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestControllerAdvice
public class GlobalExceptionHandler extends Throwable {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(UserAlreadyExistException.class)
    @ResponseStatus(HttpStatus.ALREADY_REPORTED)
    public ResponseEntity<ErrorResponseDto> handleUserAlreadyExistsException(final UserAlreadyExistException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(ex.getMessage());
        errorResponseDto.setDebugMessage("User already exists");

        return ResponseEntity.of(Optional.of(errorResponseDto));
    }


    @ExceptionHandler(EmailAlreadyTakenException.class)
    @ResponseStatus(HttpStatus.ALREADY_REPORTED)
    public ResponseEntity<ErrorResponseDto> handleEmailAlreadyTakenException(final EmailAlreadyTakenException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(ex.getMessage());
        errorResponseDto.setDebugMessage("Email Already Taken");

        return ResponseEntity.of(Optional.of(errorResponseDto));
    }

    @ExceptionHandler(EmailNotValidException.class)
    public ResponseEntity<GlobalResponse<String>> handleEmailNotValidException(EmailNotValidException ex) {
        GlobalResponse<String> response = new GlobalResponse<>();
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setMessage(ex.getMessage());
        response.setData(null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({TokenNotFoundException.class, NoSuchElementException.class})
    @ResponseStatus(HttpStatus.ALREADY_REPORTED)
    public ResponseEntity<ErrorResponseDto> handleTokenNotFoundException(final TokenNotFoundException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(ex.getMessage());
        errorResponseDto.setDebugMessage("Token Not Found");

        return ResponseEntity.of(Optional.of(errorResponseDto));
    }

    @ExceptionHandler(ConfirmationTokenException.class)
    @ResponseStatus(HttpStatus.ALREADY_REPORTED)
    public ResponseEntity<ErrorResponseDto> handleConfirmationTokenException(final ConfirmationTokenException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(ex.getMessage());
        errorResponseDto.setDebugMessage("Email Already Confirmed");

        return ResponseEntity.of(Optional.of(errorResponseDto));
    }


    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(HttpStatus.ALREADY_REPORTED)
    public ResponseEntity<ErrorResponseDto> handleTokenExpiredException(final TokenExpiredException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(ex.getMessage());
        errorResponseDto.setDebugMessage("Token Expired");

        return ResponseEntity.of(Optional.of(errorResponseDto));
    }


    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.ALREADY_REPORTED)
    public ResponseEntity<ErrorResponseDto> handleUserNotFoundException(final UserNotFoundException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(ex.getMessage());
        errorResponseDto.setDebugMessage("User Not Found");

        return ResponseEntity.of(Optional.of(errorResponseDto));
    }

    @ExceptionHandler(IncorrectDetailsException.class)
    public ResponseEntity<GlobalResponse<String>> handleIncorrectDetailsException(IncorrectDetailsException ex) {
        GlobalResponse<String> response = new GlobalResponse<>();
        response.setStatus(HttpStatus.UNAUTHORIZED);
        response.setMessage(ex.getMessage());
        response.setData(null);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ErrorException.class)
    public ResponseEntity<GlobalResponse>  handleErrorExceptionException(final ErrorException ex) {
        GlobalResponse<String> response = new GlobalResponse<>();
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setDebugMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);


    }

    @ExceptionHandler(InvalidAmountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDto> handleInvalidAmountException(final InvalidAmountException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(ex.getMessage());
        errorResponseDto.setDebugMessage("Invalid Amount!");

        return ResponseEntity.of(Optional.of(errorResponseDto));
    }

    @ExceptionHandler(IncorrectTransactionPinException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDto> handleInsufficientBalanceException(final IncorrectTransactionPinException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(ex.getMessage());
        errorResponseDto.setDebugMessage("Incorrect Transaction PIN!");

        return ResponseEntity.of(Optional.of(errorResponseDto));
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDto> handleInsufficientBalanceException(final InsufficientBalanceException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(ex.getMessage());
        errorResponseDto.setDebugMessage("Insufficient balance!");

        return ResponseEntity.of(Optional.of(errorResponseDto));
    }

    @ExceptionHandler(WalletNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDto> handleWalletNotFoundException(final WalletNotFoundException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(ex.getMessage());
        errorResponseDto.setDebugMessage("Invalid Account Number or Bank name provided!");

        return ResponseEntity.of(Optional.of(errorResponseDto));
    }

    @ExceptionHandler(InvalidPhoneNumberException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDto> handleInvalidPhoneNumberException(final InvalidPhoneNumberException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(ex.getMessage());
        errorResponseDto.setDebugMessage("Invalid Phone number");

        return ResponseEntity.of(Optional.of(errorResponseDto));
    }

    @ExceptionHandler(InvalidOtpException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDto> handleInvalidOtpException(final InvalidOtpException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(ex.getMessage());
        errorResponseDto.setDebugMessage("Invalid Otp");

        return ResponseEntity.of(Optional.of(errorResponseDto));
    }

    @ExceptionHandler(BVNException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDto> handleInvalidOtpException(final BVNException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(ex.getMessage());
        errorResponseDto.setDebugMessage("BVN Data and Supplied Data are mismatched!");

        return ResponseEntity.of(Optional.of(errorResponseDto));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(final MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DirectoryCreationFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDto> handleDirectoryCreationFailedException(final DirectoryCreationFailedException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(ex.getMessage());
        errorResponseDto.setDebugMessage("Failed to create directory!");

        return ResponseEntity.of(Optional.of(errorResponseDto));
    }

    @ExceptionHandler(QrCodeCreationFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDto> handleQrCodeCreationFailedException(final QrCodeCreationFailedException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(ex.getMessage());
        errorResponseDto.setDebugMessage("Failed to create QRCode!");

        return ResponseEntity.of(Optional.of(errorResponseDto));
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleException(Exception e) {
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(new ErrorResponse("Internal Server Error", e.getMessage(), null));
//    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<GlobalResponse<String>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        GlobalResponse<String> response = new GlobalResponse<>();
        response.setStatus(HttpStatus.UNAUTHORIZED);
        response.setMessage(ex.getMessage());
        response.setData(null);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error("Unhandled exception", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "status", "Internal Server Error",
                        "message", e.getMessage(),
                        "data", null
                ));
    }


}
