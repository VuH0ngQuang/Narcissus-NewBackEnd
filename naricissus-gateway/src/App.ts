import express from 'express';
import userRoutes from './routes/UserRoutes';
import { errorHandler } from './middlewares/ErrorHandler';

const app = express();

app.use(express.json());

// Routes
app.use('/api/users', userRoutes);

app.use(errorHandler);

export default app;