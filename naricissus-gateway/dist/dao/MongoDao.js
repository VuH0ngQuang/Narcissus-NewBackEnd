"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.mongoDao = exports.MongoDao = void 0;
const Database_1 = require("../config/Database");
// Create schema matching the Java model
const userSchema = new Database_1.mongoose.Schema({
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
const UserModel = Database_1.mongoose.model('users', userSchema);
class MongoDao {
    async findById(id) {
        return await UserModel.findOne({ userId: id });
    }
}
exports.MongoDao = MongoDao;
exports.mongoDao = new MongoDao();
