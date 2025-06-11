import dotenv from 'dotenv';
import app from './App.js';
import config from './config/Config.js';

dotenv.config();

app.listen(config.port, () => {
  console.log(`Server running on port ${config.port}`);
});