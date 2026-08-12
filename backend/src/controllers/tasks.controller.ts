import { Request, Response, NextFunction } from 'express';
import { prisma } from '../db/prisma';

export const getTasks = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const { due_date, attached_alarm_id } = req.query;
    
    // Type checking for Prisma where clause
    const where: any = {};
    if (due_date) where.due_date = new Date(due_date as string);
    if (attached_alarm_id) where.attached_alarm_id = attached_alarm_id as string;

    const tasks = await prisma.task.findMany({
      where,
      orderBy: { created_at: 'desc' }
    });
    res.json({ success: true, data: tasks });
  } catch (error) {
    next(error);
  }
};

export const createTask = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const { title, description, due_date, due_time, priority, category, attached_alarm_id } = req.body;
    const newTask = await prisma.task.create({
      data: {
        title,
        description,
        due_date: due_date ? new Date(due_date) : null,
        due_time,
        priority: priority ? parseInt(priority) : 0,
        category,
        attached_alarm_id
      }
    });
    res.status(201).json({ success: true, data: newTask });
  } catch (error) {
    next(error);
  }
};

export const updateTask = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const { id } = req.params;
    const data = { ...req.body };
    if (data.due_date) data.due_date = new Date(data.due_date);
    if (data.priority) data.priority = parseInt(data.priority);

    const updatedTask = await prisma.task.update({
      where: { id },
      data
    });
    res.json({ success: true, data: updatedTask });
  } catch (error) {
    next(error);
  }
};

export const toggleTaskCompletion = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const { id } = req.params;
    const task = await prisma.task.findUnique({ where: { id } });
    if (!task) {
      return res.status(404).json({ success: false, message: 'Task not found' });
    }
    const updatedTask = await prisma.task.update({
      where: { id },
      data: { is_completed: !task.is_completed }
    });
    res.json({ success: true, data: updatedTask });
  } catch (error) {
    next(error);
  }
};

export const deleteTask = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const { id } = req.params;
    await prisma.task.delete({ where: { id } });
    res.json({ success: true, message: 'Task deleted' });
  } catch (error) {
    next(error);
  }
};
