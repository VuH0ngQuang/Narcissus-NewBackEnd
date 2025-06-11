import { Router, RequestHandler } from "express";
import { getUserById } from "../services/UserService";

const router = Router();

router.get('/:userId', getUserById as RequestHandler);

export default router;