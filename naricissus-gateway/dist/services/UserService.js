"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.getUserById = void 0;
const RedisDao_1 = require("../dao/RedisDao");
const MongoDao_1 = require("../dao/MongoDao");
const RedisKeyEnum_1 = require("../enums/RedisKeyEnum");
const getUserById = async (req, res, next) => {
    try {
        const { userId } = req.params;
        if (!userId) {
            return res.status(400).json({ message: "User ID is required" });
        }
        else {
            let user = await RedisDao_1.redisDao.get(RedisKeyEnum_1.RedisKeyEnum.USERS.toString(), userId);
            if (!user) {
                user = await MongoDao_1.mongoDao.findById(userId);
                if (user) {
                    await RedisDao_1.redisDao.save(RedisKeyEnum_1.RedisKeyEnum.USERS.toString(), userId, user);
                }
            }
            if (!user) {
                return res.status(404).json({ message: "User not found" });
            }
            return res.status(200).json({ user });
        }
    }
    catch (error) {
        next(error);
    }
};
exports.getUserById = getUserById;
