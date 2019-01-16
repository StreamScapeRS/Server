package com.StreamScape.drops;

public class DropObject {

	// The id of the item being dropped.
	private final int id;

	// The amount of kills before the item is dropped.
	private final int ratio;

	// The lowest amount of items that can drop of this id.
	private final int low;

	// The highest amount of items that can drop of this id.
	private final int high;

	private final String itemRarity;

	public DropObject(int id, int ratio, int low, int high) {
		this(id, ratio, low, high, "");
	}

	public DropObject(int id, int ratio, int low, int high, String itemRarity) {
		this.id = id;
		this.ratio = ratio;
		this.low = low;
		this.high = high;
		this.itemRarity = itemRarity;
	}

	public int getId() {
		return id;
	}

	public int getRatio() {
		return ratio;
	}

	public int getLow() {
		return low;
	}

	public int getHigh() {
		return high;
	}

	public String itemRarity() {
		return itemRarity;
	}

}
