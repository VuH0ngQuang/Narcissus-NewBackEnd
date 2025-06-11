"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const dotenv_1 = __importDefault(require("dotenv"));
dotenv_1.default.config();
const App_1 = __importDefault(require("./App"));
const Config_1 = __importDefault(require("./config/Config"));
App_1.default.listen(Config_1.default.port, () => {
    console.log(`Server running on port ${Config_1.default.port}`);
});
