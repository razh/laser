package com.razh.laser;

import com.badlogic.gdx.scenes.scene2d.Action;

import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.utils.Array;

public class ActionFactory {
	public static Action createAction(Action action) {
		Action newAction = null;

		if (action instanceof TemporalAction) {
			if (action instanceof MoveToAction) {
				newAction = new MoveToAction();
				((MoveToAction) newAction).setX(((MoveToAction) action).getX());
				((MoveToAction) newAction).setY(((MoveToAction) action).getY());
			} else if (action instanceof MoveByAction) {
				newAction = new MoveByAction();
				((MoveByAction) newAction).setAmountX(((MoveByAction) action).getAmountX());
				((MoveByAction) newAction).setAmountY(((MoveByAction) action).getAmountY());
			} else if (action instanceof SizeToAction) {
				newAction = new SizeToAction();
				((SizeToAction) newAction).setWidth(((SizeToAction) action).getWidth());
				((SizeToAction) newAction).setHeight(((SizeToAction) action).getHeight());
			} else if (action instanceof SizeByAction) {
				newAction = new SizeByAction();
				((SizeByAction) newAction).setAmountWidth(((SizeByAction) action).getAmountWidth());
				((SizeByAction) newAction).setAmountHeight(((SizeByAction) action).getAmountHeight());
			} else if (action instanceof RotateToAction) {
				newAction = new RotateToAction();
				((RotateToAction) newAction).setRotation(((RotateToAction) action).getRotation());
			} else if (action instanceof RotateByAction) {
				newAction = new RotateByAction();
				((RotateByAction) newAction).setAmount(((RotateByAction) action).getAmount());
			} else if (action instanceof ColorAction) {
				newAction = new ColorAction();
				((ColorAction) newAction).setEndColor(((ColorAction) action).getEndColor());
			} else if (action instanceof AlphaAction) {
				newAction = new AlphaAction();
				((AlphaAction) newAction).setAlpha(((AlphaAction) action).getAlpha());
			}

			((TemporalAction) newAction).setDuration(((TemporalAction) action).getDuration());
			((TemporalAction) newAction).setInterpolation(((TemporalAction) action).getInterpolation());
		}

		// Delegate actions.
		else if (action instanceof DelegateAction) {
			if (action instanceof DelayAction) {
				newAction = new DelayAction();
				((DelayAction) newAction).setDuration(((DelayAction) action).getDuration());
			} else if (action instanceof RepeatAction) {
				newAction = new RepeatAction();
				((RepeatAction) newAction).setCount(((RepeatAction) action).getCount());
			} else if (action instanceof AfterAction) {
				newAction = new AfterAction();
			}

			((DelegateAction) newAction).setAction(ActionFactory.createAction(((DelegateAction) action).getAction()));
		}

		// Parallel actions.
		else if (action instanceof ParallelAction) {
			if (action instanceof SequenceAction) {
				newAction = new SequenceAction();
			} else {
				newAction = new ParallelAction();
			}

			Array<Action> actions = ((ParallelAction) action).getActions();
			for (int i = 0; i < actions.size; i++) {
				((ParallelAction) newAction).addAction(ActionFactory.createAction(actions.get(i)));
			}
		}

		else if (action instanceof VisibleAction) {
			newAction = new VisibleAction();
			((VisibleAction) newAction).setVisible(((VisibleAction) action).isVisible());
		}

		else if (action instanceof TouchableAction) {
			newAction = new TouchableAction();
			((TouchableAction) newAction).setTouchable(((TouchableAction) action).getTouchable());
		}

		else if (action instanceof AddAction) {
			newAction = new AddAction();
		}

		else if (action instanceof RemoveActorAction) {
			newAction = new RemoveActorAction();
		}

		else if (action instanceof RunnableAction) {
			newAction = new RunnableAction();
			((RunnableAction) newAction).setRunnable(((RunnableAction) action).getRunnable());
		}

		return newAction;
	}
}
