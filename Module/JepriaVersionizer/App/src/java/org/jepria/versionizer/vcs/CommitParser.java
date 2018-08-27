package org.jepria.versionizer.vcs;

import java.util.List;

import org.jepria.versionizer.ReleaseNote;

/**
 * Parses commits (implementation is VCS-dependent)
 */
public interface CommitParser {
  List<ReleaseNote> extractReleaseNotes(CommitFacade commit);
}
