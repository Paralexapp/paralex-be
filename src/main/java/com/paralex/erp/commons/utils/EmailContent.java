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
}
