package com.paralex.erp.commons.utils;

public class EmailContent {

    public static String forgotPasswordEmail(String customer, String url){
        return "<p> Dear "+ customer+ ", </p>"+
                "<p>You are receiving this email because a request was made to rest your password for your Paralex Application." +
                " To reset your password please follow the instructions below: </p>"+
                "<p>1. Click on the following link to reset your password <strong>"+url+"</strong>.</p>"+
                "<p>2. If the above link doesn't work, copy and paste the following url into your browser's address bar <strong>"+url+"</strong>.</p>"+
                "<p>Please ignore this email if you did not request a password reset.</p>"+
                "<br>"+
                "<p>Thank you,</p>"+
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
