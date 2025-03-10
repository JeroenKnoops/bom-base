/*
 * Copyright (c) 2020-2021, Koninklijke Philips N.V., https://www.philips.com
 * SPDX-License-Identifier: MIT
 */

package com.philips.research.bombase.core.downloader;

import java.net.URI;
import java.nio.file.Path;
import java.util.function.Consumer;

/**
 * Makes files available in a directory local to this machine for processing.
 * <p>
 * The download URI format is: {@code <vcs_tool>+<transport>://<host_name>[/<path_to_repository>][@<revision_tag_or_branch>][#<sub_path>]}
 */
public interface DownloadService {
    /**
     * Downloads a file or directory structure from an online location.
     *
     * @param location download location
     * @param callback lambda to invoke with the downloaded base directory
     */
    void download(URI location, Consumer<Path> callback);
}
