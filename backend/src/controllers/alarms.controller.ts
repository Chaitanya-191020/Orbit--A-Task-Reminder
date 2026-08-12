import { Request, Response, NextFunction } from 'express';
import { prisma } from '../db/prisma';

export const getAlarms = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const alarms = await prisma.alarm.findMany({
      include: { tasks: true },
      orderBy: { created_at: 'desc' }
    });
    res.json({ success: true, data: alarms });
  } catch (error) {
    next(error);
  }
};

export const getAlarmById = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const { id } = req.params;
    const alarm = await prisma.alarm.findUnique({
      where: { id },
      include: { tasks: true }
    });
    if (!alarm) {
      return res.status(404).json({ success: false, message: 'Alarm not found' });
    }
    res.json({ success: true, data: alarm });
  } catch (error) {
    next(error);
  }
};

export const createAlarm = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const { label, alarm_time, repeat_days, sound_uri, vibrate, snooze_duration_minutes } = req.body;
    const newAlarm = await prisma.alarm.create({
      data: { label, alarm_time, repeat_days, sound_uri, vibrate, snooze_duration_minutes }
    });
    res.status(201).json({ success: true, data: newAlarm });
  } catch (error) {
    next(error);
  }
};

export const updateAlarm = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const { id } = req.params;
    const updatedAlarm = await prisma.alarm.update({
      where: { id },
      data: req.body
    });
    res.json({ success: true, data: updatedAlarm });
  } catch (error) {
    next(error);
  }
};

export const toggleAlarmStatus = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const { id } = req.params;
    const alarm = await prisma.alarm.findUnique({ where: { id } });
    if (!alarm) {
      return res.status(404).json({ success: false, message: 'Alarm not found' });
    }
    const updatedAlarm = await prisma.alarm.update({
      where: { id },
      data: { is_enabled: !alarm.is_enabled }
    });
    res.json({ success: true, data: updatedAlarm });
  } catch (error) {
    next(error);
  }
};

export const deleteAlarm = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const { id } = req.params;
    await prisma.alarm.delete({ where: { id } });
    res.json({ success: true, message: 'Alarm deleted' });
  } catch (error) {
    next(error);
  }
};
