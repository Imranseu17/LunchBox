package com.thelunchbox.bd.enum_class

enum class RegistrationEnum(var key: Int, var code: Int)  {
    REGISTRATION_SUCCESS(1, 201), REGISTRATION_FAILED(2, 501), SERVER_ERROR(3, 999);

    companion object {
        fun getByCode(code: Int): RegistrationEnum? {
            for (rs in RegistrationEnum.values()) {
                if (rs.code == code) return rs
            }
            return null
        }
    }
}