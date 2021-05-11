package com.dntask.userphonebooks.controller;

class ControllerUtils {
    public static class ExceptionMessage {
        private final String message;

        public ExceptionMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
