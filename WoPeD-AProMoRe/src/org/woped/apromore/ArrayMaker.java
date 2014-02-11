package org.woped.apromore;

import java.util.List;

import org.apromore.model.ProcessSummaryType;
import org.apromore.model.VersionSummaryType;


public class ArrayMaker {

	/**
	 * @param args
	 */
	public static String[][] run(List<ProcessSummaryType> list) {

		String[][] s = new String[list.size()][5];

		for (int i = 0; i < list.size(); i++) {
			s[i][0] = "" + list.get(i).getName();
			s[i][1] = "" + list.get(i).getId();
			s[i][2] = "" + list.get(i).getOwner();
			s[i][3] = "" + list.get(i).getOriginalNativeType();
			List<VersionSummaryType> b = list.get(i).getVersionSummaries();
			s[i][4] = "";
			for (int z = 0; z < b.size() - 1; z++) {
				s[i][4] = s[i][4] + b.get(z).getName() + "; ";
			}
			s[i][4] = s[i][4] + b.get(b.size() - 1).getName();
		}
		return s;
	}

	public static String[][] run(List<ProcessSummaryType> list, String name,
			int id, String owner, String type) {

		int j = list.size() - 1;

		if (!((name.equalsIgnoreCase("")) && (id == 0)
				&& (owner.equalsIgnoreCase("")) && (type.equalsIgnoreCase("")))) {
			j = 0;
			for (int i = 0; i < list.size() - 1; i++) {
				if ((list.get(i).getName().toLowerCase()
						.contains(name.toLowerCase()) || name
						.equalsIgnoreCase(""))
						&& (list.get(i).getDomain().toLowerCase()
								.contains(type.toLowerCase()) || type
								.equalsIgnoreCase(""))
						&& (list.get(i).getOwner().toLowerCase()
								.contains(owner.toLowerCase()) || owner
								.equalsIgnoreCase(""))
						&& ((list.get(i).getId() == id) || (id == 0)))
					j++;
			}
		}

		String[][] s = new String[j][5];

		int k = 0;

		for (int i = 0; i < list.size() - 1; i++) {
			if (!((name.equalsIgnoreCase("")) && (id == 0)
					&& (owner.equalsIgnoreCase("")) && (type
						.equalsIgnoreCase("")))) {

				if ((list.get(i).getName().toLowerCase()
						.contains(name.toLowerCase()) || name
						.equalsIgnoreCase(""))
						&& (list.get(i).getDomain().toLowerCase()
								.contains(type.toLowerCase()) || type
								.equalsIgnoreCase(""))
						&& (list.get(i).getOwner().toLowerCase()
								.contains(owner.toLowerCase()) || owner
								.equalsIgnoreCase(""))
						&& ((list.get(i).getId() == id) || (id == 0))) {
					s[k][0] = "" + list.get(i).getName();
					s[k][1] = "" + list.get(i).getId();
					s[k][2] = "" + list.get(i).getOwner();
					s[k][3] = "" + list.get(i).getDomain();
					List<VersionSummaryType> b = list.get(i)
							.getVersionSummaries();
					s[k][4] = "";
					for (int z = 0; z < b.size() - 1; z++) {
						s[k][4] = s[k][4] + b.get(z).getName() + "; ";
					}
					s[k][4] = s[k][4] + b.get(b.size() - 1).getName();

					k++;
				}
			} else {
				s[i][0] = "" + list.get(i).getName();
				s[i][1] = "" + list.get(i).getId();
				s[i][2] = "" + list.get(i).getOwner();
				s[i][3] = "" + list.get(i).getDomain();
				List<VersionSummaryType> b = list.get(i).getVersionSummaries();
				s[i][4] = "";
				for (int z = 0; z < b.size() - 1; z++) {
					s[i][4] = s[i][4] + b.get(z).getName() + "; ";
				}
				s[i][4] = s[i][4] + b.get(b.size() - 1).getName();
			}

		}
		return s;
	}

}
