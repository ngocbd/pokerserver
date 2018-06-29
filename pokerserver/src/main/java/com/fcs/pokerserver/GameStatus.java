package com.fcs.pokerserver;

/**
 * Enum to track the status of the Game from the perspective of the game organizer/controller.
 * 
 * @author ngocbd
 * Copyright (c) 2018
 */

public enum GameStatus {
	NOT_STARTED,
	SEATING,
	PREFLOP,
	FLOP,
	TURN,
	RIVER,
	END_HAND
}