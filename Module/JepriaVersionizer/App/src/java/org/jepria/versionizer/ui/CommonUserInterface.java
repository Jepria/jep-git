package org.jepria.versionizer.ui;

import org.jepria.versionizer.SearchOverviewFilesAction;
import org.jepria.versionizer.UserManualGit;
import org.jepria.versionizer.UserManualSvn;
import org.jepria.versionizer.UserManualTestDriver;
import org.jepria.versionizer.WriteReleaseNotesAction;

public interface CommonUserInterface extends 
    SearchOverviewFilesAction.UserInterface, 
    WriteReleaseNotesAction.UserInterface,
    UserManualSvn.UserInterface,
    UserManualGit.UserInterface, 
    UserManualTestDriver.UserInterface {
}
