package com.thelunchbox.bd.enum_class

enum class LoginEnum(var key: Int, var code: Int) {
    LOGIN_SUCCESS(1, 201), LOGIN_FAILED(2, 501), SERVER_ERROR(3, 999);

    companion object {
        fun getByCode(code: Int): LoginEnum? {
            for (rs in values()) {
                if (rs.code == code) return rs
            }
            return null
        }
    }
}