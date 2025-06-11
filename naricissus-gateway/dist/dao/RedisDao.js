"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.redisDao = exports.RedisDao = void 0;
const Database_1 = require("../config/Database");
class RedisDao {
    async save(key, id, data) {
        await Database_1.redisClient.set(`${key}:${id}`, JSON.stringify(data));
    }
    async get(key, id) {
        const data = await Database_1.redisClient.get(`${key}:${id}`);
        return data ? JSON.parse(data) : null;
    }
}
exports.RedisDao = RedisDao;
exports.redisDao = new RedisDao();
