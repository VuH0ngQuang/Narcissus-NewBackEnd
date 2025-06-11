"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = require("express");
const UserService_1 = require("../services/UserService");
const router = (0, express_1.Router)();
router.get('/:userId', UserService_1.getUserById);
exports.default = router;
