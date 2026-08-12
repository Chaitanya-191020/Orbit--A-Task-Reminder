import dotenv from 'dotenv';
dotenv.config();

import app from './app';
import { prisma } from './db/prisma';

const PORT = process.env.PORT || 5000;

async function startServer() {
  try {
    // Test Database connection
    await prisma.$connect();
    console.log('✅ Connected to PostgreSQL database via Prisma.');

    app.listen(PORT, () => {
      console.log(`🚀 Server is running on http://localhost:${PORT}`);
    });
  } catch (error) {
    console.error('❌ Failed to start the server:', error);
    process.exit(1);
  }
}

startServer();

process.on('SIGINT', async () => {
  await prisma.$disconnect();
  console.log('Prisma client disconnected on app termination');
  process.exit(0);
});
