/*******************************************************************************
 * Copyright (C) 2007 The University of Manchester   
 * 
 *  Modifications to the initial code base are copyright of their
 *  respective authors, or their employers as appropriate.
 * 
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2.1 of
 *  the License, or (at your option) any later version.
 *    
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *    
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomart;

import java.util.List;

import net.sf.taverna.t2.visit.VisitReport;

import net.sf.taverna.t2.workflowmodel.health.RemoteHealthChecker;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;
import net.sf.taverna.t2.workflowmodel.processor.activity.DisabledActivity;


import org.biomart.martservice.MartQuery;
import org.biomart.martservice.MartServiceXMLHandler;
import org.jdom.Element;

public class BiomartActivityHealthChecker extends RemoteHealthChecker {

	public boolean canVisit(Object subject) {
		if (subject == null) {
			return false;
		}
		if (subject instanceof BiomartActivity) {
			return true;
		}
		if (subject instanceof DisabledActivity) {
			return (((DisabledActivity) subject).getActivity() instanceof BiomartActivity);
		}
		return false;
	}

	public VisitReport visit(Object o, List<Object> ancestors) {
		Element biomartQueryElement = null;
		Activity activity = (Activity) o;
		if (activity instanceof BiomartActivity) {
			biomartQueryElement = ((BiomartActivity)activity).getConfiguration();
		} else if (activity instanceof DisabledActivity) {
			biomartQueryElement = (Element) ((DisabledActivity) activity).getActivityConfiguration();
		}
		MartQuery biomartQuery = MartServiceXMLHandler.elementToMartQuery(biomartQueryElement, null);
		return contactEndpoint(activity, biomartQuery.getMartService().getLocation());
	}
	

}
