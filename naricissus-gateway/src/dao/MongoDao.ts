import { mongoose } from '../config/Database';
import UserEntity from '../models/user/User';

// Create schema matching the Java model
const userSchema = new mongoose.Schema({
    userId: { type: String, required: true },
    email: { type: String, required: true },
    password: { type: String, required: true },
    userName: { type: String, required: true },
    phoneNumber: { type: String },
    address: { type: String },
    date: { type: Date },
    role: { type: String, enum: ['ADMIN', 'USER'] }
}, { collection: 'users' }); // Explicitly set collection name to match Java @Document

// Create User Model
const UserModel = mongoose.model<UserEntity>('users', userSchema);

export class MongoDao {
    async findById(id: string): Promise<UserEntity | null> {
        return await UserModel.findOne({ userId: id });
    }
}

export const mongoDao = new MongoDao(); 