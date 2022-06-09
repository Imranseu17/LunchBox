package com.thelunchbox.bd.enum_class

enum class ErrorCode(var key: Int, var code: Int) {
    LOGOUTERROR(1, 401), ERRORCODE500(2, 500), ERRORCODE400(3, 400), ERRORCODE406(
        4,
        406
    ),
    ERRORCODE412(5, 412), ERRORCODE455(5, 455), SERVER_ERROR_CODE(7, 999);

    companion object {
        fun getByCode(code: Int): ErrorCode? {
            for (rs in values()) {
                if (rs.code == code) return rs
            }
            return null
        }
    }
}