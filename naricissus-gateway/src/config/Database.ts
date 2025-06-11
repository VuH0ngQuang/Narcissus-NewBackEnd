import { createClient } from 'redis';
import mongoose from 'mongoose';
import { env } from 'process';

// Redis configuration
const redisClient = createClient({
    url: process.env.REDIS_URL || 'redis://localhost:6379',
    password: process.env.REDIS_PW || undefined
});

// Connecting to the Redis server
async function connectRedis(): Promise<void> {
    try {
        await redisClient.connect();
        console.log('Successfully connected to Redis');
    } catch (error) {
        console.error('Failed to connect to Redis:', error);
    }
}

// Error handling
redisClient.on('error', (err: Error) => {
    console.error('Redis connection error:', err);
});

connectRedis().catch(console.error);

// MongoDB configuration
const mongoURI = process.env.MONGODB_URI || 'mongodb://localhost:27017/narcissus';

mongoose.connect(mongoURI)
    .then(() => console.log('MongoDB Connected'))
    .catch((err) => console.error('MongoDB Connection Error:', err));

export { redisClient, mongoose }; 