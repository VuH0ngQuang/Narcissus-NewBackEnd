"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.RedisKeyEnum = void 0;
var RedisKeyEnum;
(function (RedisKeyEnum) {
    RedisKeyEnum[RedisKeyEnum["PRODUCT"] = 0] = "PRODUCT";
    RedisKeyEnum[RedisKeyEnum["ORDERS"] = 1] = "ORDERS";
    RedisKeyEnum[RedisKeyEnum["USERS"] = 2] = "USERS";
    RedisKeyEnum[RedisKeyEnum["EMAIL_USER"] = 3] = "EMAIL_USER";
})(RedisKeyEnum || (exports.RedisKeyEnum = RedisKeyEnum = {}));
