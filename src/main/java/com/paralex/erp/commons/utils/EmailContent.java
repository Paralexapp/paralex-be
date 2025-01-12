package com.paralex.erp.commons.utils;

public class EmailContent {

    public static String forgotPasswordEmail(String customer, String url, String otp) {
        return "<p>Dear " + customer + ",</p>" +
                "<p>You are receiving this email because a request was made to reset your password for your Paralex Application. To reset your password, please follow one of the options below:</p>" +
                "<p>1. Click on the following link to reset your password: <strong><a href=\"" + url + "\">Reset Password</a></strong>.</p>" +
                "<p>2. If the above link doesn't work, copy and paste the following URL into your browser's address bar: <strong>" + url + "</strong>.</p>" +
                "<p>3. Alternatively, you can use the following OTP to reset your password: <strong>" + otp + "</strong>.</p>" +
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
