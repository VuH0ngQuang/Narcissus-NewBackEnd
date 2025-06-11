"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.mongoose = exports.redisClient = void 0;
const redis_1 = require("redis");
const mongoose_1 = __importDefault(require("mongoose"));
exports.mongoose = mongoose_1.default;
// Redis configuration
const redisClient = (0, redis_1.createClient)({
    url: process.env.REDIS_URL || 'redis://localhost:6379',
    password: process.env.REDIS_PW || undefined
});
exports.redisClient = redisClient;
// Connecting to the Redis server
async function connectRedis() {
    try {
        await redisClient.connect();
        console.log('Successfully connected to Redis');
    }
    catch (error) {
        console.error('Failed to connect to Redis:', error);
    }
}
// Error handling
redisClient.on('error', (err) => {
    console.error('Redis connection error:', err);
});
connectRedis().catch(console.error);
// MongoDB configuration
const mongoURI = process.env.MONGODB_URI || 'mongodb://localhost:27017/narcissus';
mongoose_1.default.connect(mongoURI)
    .then(() => console.log('MongoDB Connected'))
    .catch((err) => console.error('MongoDB Connection Error:', err));
