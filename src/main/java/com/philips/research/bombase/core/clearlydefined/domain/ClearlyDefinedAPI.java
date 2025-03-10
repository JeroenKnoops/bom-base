/*
 * Copyright (c) 2020-2021, Koninklijke Philips N.V., https://www.philips.com
 * SPDX-License-Identifier: MIT
 */

package com.philips.research.bombase.core.clearlydefined.domain;

import pl.tlinkowski.annotation.basic.NullOr;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface ClearlyDefinedAPI {
    @GET("definitions/{type}/{provider}/{namespace}/{name}/{revision}")
    Call<ResponseJson> getDefinition(@Path("type") String type, @Path("provider") String provider, @Path("namespace") String namespace,
                                     @Path("name") String name, @Path("revision") String revision);

    @SuppressWarnings("NotNullFieldNotInitialized")
    class ResponseJson implements PackageDefinition {
        DescribedJson described;
        LicensedJson licensed;
        @NullOr ScoresJson scores;

        @Override
        public boolean isValid() {
            return (scores != null) && scores.effective > 0;
        }

        @Override
        public int getDescribedScore() {
            return described.score.total;
        }

        @Override
        public int getLicensedScore() {
            return licensed.score.total;
        }

        @Override
        public Optional<String> getTitle() {
            return described.getName();
        }

        @Override
        public Optional<URI> getDownloadLocation() {
            return described.getDownloadLocation();
        }

        @Override
        public Optional<String> getSourceLocation() {
            return described.getSourceLocation();
        }

        @Override
        public Optional<URI> getHomepage() {
            return described.getHomepage();
        }

        @Override
        public Optional<List<String>> getAuthors() {
            final var list = licensed.getAttribution();
            return list.isEmpty() ? Optional.empty() : Optional.of(list);
        }

        @Override
        public Optional<String> getDeclaredLicense() {
            return licensed.getDeclaredLicense();
        }

        @Override
        public Optional<List<String>> getDetectedLicenses() {
            final var list = licensed.getDetectedLicenses();
            return list.isEmpty() ? Optional.empty() : Optional.of(list);
        }

        @Override
        public Optional<String> getSha1() {
            return described.getSha1();
        }

        @Override
        public Optional<String> getSha256() {
            return described.getSha256();
        }
    }

    class DescribedJson {
        @NullOr SourceLocationJson sourceLocation;
        @NullOr UrlJson urls;
        @NullOr HashJson hashes;
        @NullOr URI projectWebsite;
        ScoreJson score;

        Optional<String> getName() {
            return Optional.ofNullable((sourceLocation != null && sourceLocation.name != null) ? sourceLocation.name : null);
        }

        Optional<URI> getDownloadLocation() {
            return Optional.ofNullable((urls != null && urls.download != null) ? urls.download : null);
        }

        Optional<String> getSourceLocation() {
            return Optional.ofNullable((sourceLocation != null && sourceLocation.url != null) ? sourceLocation.url : null);
        }

        Optional<URI> getHomepage() {
            return Optional.ofNullable(projectWebsite);
        }

        Optional<String> getSha1() {
            return Optional.ofNullable((hashes != null && hashes.sha1 != null) ? hashes.sha1 : null);
        }

        Optional<String> getSha256() {
            return Optional.ofNullable((hashes != null && hashes.sha256 != null) ? hashes.sha256 : null);
        }
    }

    class SourceLocationJson {
        @NullOr String name;
        @NullOr String url;
    }

    class UrlJson {
        @NullOr URI download;
    }

    class HashJson {
        @NullOr String sha1;
        @NullOr String sha256;
    }

    class LicensedJson {
        @NullOr String declared;
        @NullOr FacetsJson facets;
        ScoreJson score;

        Optional<String> getDeclaredLicense() {
            if ("NOASSERTION".equals(declared)) {
                return Optional.empty();
            }
            return Optional.ofNullable(declared);
        }

        List<String> getDetectedLicenses() {
            return listFromFirstCoreFacet(FacetJson::getExpressions);
        }

        List<String> getAttribution() {
            return listFromFirstCoreFacet(FacetJson::getAttribution);
        }

        private List<String> listFromFirstCoreFacet(Function<@NullOr FacetJson, List<String>> accessor) {
            return (facets != null && facets.core != null)
                    ? accessor.apply(facets.core)
                    : List.of();
        }
    }

    class FacetsJson {
        @NullOr FacetJson core;
    }

    class FacetJson {
        @NullOr AttributionJson attribution;
        @NullOr DiscoveredJson discovered;

        List<String> getAttribution() {
            return (attribution != null) ? attribution.parties : List.of();
        }

        List<String> getExpressions() {
            return (discovered != null) ? discovered.expressions : List.of();
        }
    }

    class AttributionJson {
        List<String> parties = new ArrayList<>();
    }

    class DiscoveredJson {
        List<String> expressions = new ArrayList<>();
    }

    class ScoreJson {
        int total;
    }

    class ScoresJson {
        int effective;
    }
}
