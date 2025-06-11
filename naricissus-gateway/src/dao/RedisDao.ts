import {redisClient} from '../config/Database'

export class RedisDao {
    async save(key: string, id: string, data: any): Promise<void> {
        await redisClient.set(`${key}:${id}`, JSON.stringify(data));
    }

    async get(key: string, id: string): Promise<any> {
        const data = await redisClient.get(`${key}:${id}`);
        return data ? JSON.parse(data) : null;
    }
}

export const redisDao = new RedisDao(); 