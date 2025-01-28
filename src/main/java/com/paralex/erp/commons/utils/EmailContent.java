package com.paralex.erp.commons.utils;

public class EmailContent {

    public static String forgotPasswordEmail(String customer, String otp) {
        return "<p>Dear " + customer + ",</p>" +
                "<p>You are receiving this email because a request was made to reset your password for your Paralex Application. To reset your password, please use the following OTP:</p>" +
                "<p><strong>" + otp + "</strong></p>" +
                "<p>Please ignore this email if you did not request a password reset.</p>" +
                "<br>" +
                "<p>Thank you,</p>" +
                "<p>Paralex.</p>";
    }



    public static String verificationEmail(String otp){
        return "<p> Dear  customer, </p>"+
                "<p>This is your verification code: <strong>"+otp+"</strong>.</p>"+
                "<br>"+
                "<p>Thank you,</p>"+
                "<p>Paralex.</p>";
    }

    public static final String HEADER_SIGNATURE_DELIVERY_CONTENT = "<html>"
            + "<head>"
            + "<style>"
            + "body {"
            + "  font-family: Arial, Helvetica, sans-serif;"
            + "  font-size: 1rem;"
            + "  line-height: 1.6;"
            + "  color: #333;"
            + "  background-color: #f4f4f4;"
            + "  padding: 20px;"
            + "}"
            + ".container {"
            + "  background-color: #fff;"
            + "  border-radius: 5px;"
            + "  padding: 20px;"
            + "  margin: 0 auto;"
            + "  width: 80%;"
            + "}"
            + ".header {"
            + "  text-align: center;"
            + "  padding: 10px 0;"
            + "  border-bottom: 1px solid #ddd;"
            + "}"
            + ".header h1 {"
            + "  font-size: 2rem;"
            + "  color: #007BFF;"
            + "}"
            + ".content {"
            + "  margin-top: 20px;"
            + "}"
            + ".footer {"
            + "  margin-top: 30px;"
            + "  text-align: center;"
            + "  color: #888;"
            + "}"
            + "</style>"
            + "</head>"
            + "<body>"
            + " <div class=\"container\">"
            + "<img src='cid:pdaralexLogo' alt=\"logo\" style=\"display: block; margin: 0 auto;\">"
            + "   <div class=\"header\">"
            + "     <h1>New Header Signature Generated</h1>"
            + "   </div>"
            + "   <div class=\"content\">"
            + "     <p>Dear Tester ,</p>"
            + "     <p>We have generated a new header signature for your account. This signature is used to secure your communications with our server.</p>"
            + "     <p><strong>Header Signature 1:</strong> [[header_signature_ONE]]</p>"
            + "     <p><strong>Header Signature 2:</strong> [[header_signature_TWO]]</p>"
            + "     <p><strong>Header Signature 3:</strong> [[header_signature_THREE]]</p>"
            + "     <p>Please update your application with this new header signature. If you have any questions or need assistance, please contact our support team.</p>"
            + "     <p>Securing your header signatures is crucial to protect your data and prevent unauthorized access to your account. A strong, unique header signature helps ensure that your communications with our server are secure and trusted.</p>"
            + "   </div>"
            + "   <div class=\"footer\">"
            + "     <p>Thanks,</p>"
            + "     <p>The Paralex Team</p>"
            + "   </div>"
            + " </div>"
            + "</body>"
            + "</html>";
}
