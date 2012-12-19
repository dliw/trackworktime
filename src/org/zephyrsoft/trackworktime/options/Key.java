/*
 * This file is part of TrackWorkTime (TWT).
 *
 * TWT is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * TWT is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TWT. If not, see <http://www.gnu.org/licenses/>.
 */
package org.zephyrsoft.trackworktime.options;

import java.util.HashSet;
import java.util.Set;
import org.zephyrsoft.trackworktime.R;

/**
 * Central holder for all keys which are defined in XML files but have to be used in Java code.
 * 
 * @author Mathis Dirksen-Thedens
 */
@SuppressWarnings("javadoc")
public enum Key {
	
	ENABLE_FLEXI_TIME("keyEnableFlexiTime", DataType.BOOLEAN, null, R.string.enableFlexiTime),
	FLEXI_TIME_START_VALUE("keyFlexiTimeStartValue", DataType.HOUR_MINUTE, ENABLE_FLEXI_TIME,
		R.string.flexiTimeStartValue),
	FLEXI_TIME_TARGET("keyFlexiTimeTarget", DataType.DOUBLE, ENABLE_FLEXI_TIME, R.string.flexiTimeTarget),
	
	FLATTENING_ENABLED("keyFlatteningEnabled", DataType.BOOLEAN, null, R.string.flatteningEnabled),
	SMALLEST_TIME_UNIT("keySmallestTimeUnit", DataType.INTEGER, FLATTENING_ENABLED, R.string.smallestTimeUnit),
	
	LOCATION_BASED_TRACKING_ENABLED("keyLocationBasedTrackingEnabled", DataType.BOOLEAN, null,
		R.string.enableLocationBasedTracking),
	LOCATION_BASED_TRACKING_VIBRATE("keyLocationBasedTrackingVibrate", DataType.BOOLEAN,
		LOCATION_BASED_TRACKING_ENABLED, R.string.locationBasedTrackingVibrate),
	LOCATION_BASED_TRACKING_LATITUDE("keyLocationBasedTrackingLatitude", DataType.DOUBLE,
		LOCATION_BASED_TRACKING_ENABLED, R.string.workplaceLatitude),
	LOCATION_BASED_TRACKING_LONGITUDE("keyLocationBasedTrackingLongitude", DataType.DOUBLE,
		LOCATION_BASED_TRACKING_ENABLED, R.string.workplaceLongitude),
	LOCATION_BASED_TRACKING_TOLERANCE("keyLocationBasedTrackingTolerance", DataType.INTEGER,
		LOCATION_BASED_TRACKING_ENABLED, R.string.trackingTolerance),
	
	AUTO_PAUSE_ENABLED("keyAutoPauseEnabled", DataType.BOOLEAN, null, R.string.autoPauseEnabled),
	AUTO_PAUSE_BEGIN("keyAutoPauseBegin", DataType.TIME, AUTO_PAUSE_ENABLED, R.string.autoPauseBegin),
	AUTO_PAUSE_END("keyAutoPauseEnd", DataType.TIME, AUTO_PAUSE_ENABLED, R.string.autoPauseEnd);
	
	private final String name;
	private final DataType dataType;
	private final Key parent;
	private final Integer readableNameResourceId;
	
	private Key(String name, DataType dataType, Key parent, Integer readableNameResourceId) {
		this.name = name;
		this.dataType = dataType;
		this.parent = parent;
		this.readableNameResourceId = readableNameResourceId;
	}
	
	public String getName() {
		return name;
	}
	
	public DataType getDataType() {
		return dataType;
	}
	
	public Key getParent() {
		return parent;
	}
	
	public Integer getReadableNameResourceId() {
		return readableNameResourceId;
	}
	
	public static Key getKeyWithName(String name) {
		for (Key key : values()) {
			if (key.getName().equals(name)) {
				return key;
			}
		}
		return null;
	}
	
	public static Set<Key> getChildKeys(Key parentKey) {
		Set<Key> ret = new HashSet<Key>();
		for (Key key : values()) {
			if (key.getParent() == parentKey) {
				ret.add(key);
			}
		}
		return ret;
	}
	
}