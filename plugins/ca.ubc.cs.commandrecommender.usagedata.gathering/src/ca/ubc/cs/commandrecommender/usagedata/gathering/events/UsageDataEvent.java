/*******************************************************************************
 * Copyright (c) 2007 The Eclipse Foundation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    The Eclipse Foundation - initial API and implementation
 *******************************************************************************/
package ca.ubc.cs.commandrecommender.usagedata.gathering.events;

import java.io.Serializable;

/**
 * The {@link UsageDataEvent} class captures information about a single
 * event. Once created, instances of this class cannot be modified.
 * 
 * @author Wayne Beaton
 *
 */
public class UsageDataEvent implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The {@link #what} field describes the event that has occurred. It
	 * is dependent on the kind of thing that caused the event. As a 
	 * rule of thumb, the value indicates that something has
	 * already happened (e.g. "activated", "loaded", "clicked").
	 */
	public final String what;
	
	/**
	 * The {@link #kind} field describes the kind of thing that caused
	 * the event (e.g. "view", "workbench", "menu", "bundle").
	 */
	public final String kind;
	
	/**
	 * The {@link #description} field provides additional, kind-specific
	 * information. An event describing the activation of a view might,
	 * for example, provide the name of the view in this field.
	 */
	public final String description;
	
	/**
	 * The {@link #bundleId} field contains symbolic name of the bundle that
	 * owns the thing that caused the event.
	 */
	public final String bundleId;
	
	/**
	 * The {@link #bundleVersion} field contains the version of the bundle
	 * that owns the thing that caused the event.
	 */
	public String bundleVersion;
	
	/**
	 * The {@link #when} field contains a time stamp, expressed as
	 * milliseconds in UNIX time (using <code>System.currentTimeMillis()</code>);
	 */
	public final long when;
	
	/**
	 * The {@link #bindingUsed} field contains a string indicating whether a user used a key binding or not.
	 * Where "1" is a binding was used and "0" is a binding was not used.
	 */
	public final String bindingUsed;
	
	/**
	 * The {@link #name} field contains a string indicating the Name of the command or bundle a user has used.
	 */
	public final String name;
	
	/**
	 * The {@link #info} field contain additional info associated with a command or bundle. (ie. it's default description
	 */
	public final String info;

	public UsageDataEvent(String what, String kind, String description, String bundleId,
			String bundleVersion, String bindingUsed, long when, String name, String info) {
				this.what = what;
				this.kind = kind;
				this.description = description;
				this.bundleId = bundleId;
				this.bundleVersion = bundleVersion;
				this.when = when;
				this.bindingUsed = bindingUsed;
				this.name = name;
				this.info = info;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((bundleId == null) ? 0 : bundleId.hashCode());
		result = prime * result
				+ ((bundleVersion == null) ? 0 : bundleVersion.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((kind == null) ? 0 : kind.hashCode());
		result = prime * result + ((what == null) ? 0 : what.hashCode());
		result = prime * result + ((info == null) ? 0 : info.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((bindingUsed == null) ? 0 : bindingUsed.hashCode());
		result = prime * result + (int) (when ^ (when >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsageDataEvent other = (UsageDataEvent) obj;
		if (bundleId == null) {
			if (other.bundleId != null)
				return false;
		} else if (!bundleId.equals(other.bundleId))
			return false;
		if (bundleVersion == null) {
			if (other.bundleVersion != null)
				return false;
		} else if (!bundleVersion.equals(other.bundleVersion))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (kind == null) {
			if (other.kind != null)
				return false;
		} else if (!kind.equals(other.kind))
			return false;
		if (what == null) {
			if (other.what != null)
				return false;
		} else if (!what.equals(other.what))
			return false;

		if (bindingUsed == null) {
			if (other.bindingUsed != null)
				return false;
		} else if (!bindingUsed.equals(other.bindingUsed))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (info == null) {
			if (other.info != null)
				return false;
		} else if (!info.equals(other.info))
			return false;
		if (when != other.when)
			return false;
		return true;
	}

}
