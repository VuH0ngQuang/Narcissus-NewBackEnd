import { NextFunction, Request, Response } from "express"
import { redisDao } from "../dao/RedisDao"
import { mongoDao } from "../dao/MongoDao"
import { RedisKeyEnum } from "../enums/RedisKeyEnum"

export const getUserById = async (req: Request, res: Response, next: NextFunction) => {
    try {
        const { userId } = req.params;
        
        if (!userId) {
            return res.status(400).json({ message: "User ID is required" });
        } else {
            let user = await redisDao.get(RedisKeyEnum.USERS.toString(), userId);
            
            if (!user) {
                user = await mongoDao.findById(userId);
                if (user) {
                    await redisDao.save(RedisKeyEnum.USERS.toString(), userId, user);
                }
            }
            
            if (!user) {
                return res.status(404).json({ message: "User not found" });
            }

            return res.status(200).json({ user });
        }
    } catch (error) {
        next(error);
    }
};