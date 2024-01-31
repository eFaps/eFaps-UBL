/*
 * Copyright © 2003 - 2024 The eFaps Team (-)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.efaps.ubl.dto;

public class SignResponseDto
{

    private final String ubl;
    private final String hash;

    private SignResponseDto(final Builder builder)
    {
        ubl = builder.ubl;
        hash = builder.hash;
    }

    public String getUbl()
    {
        return ubl;
    }

    public String getHash()
    {
        return hash;
    }

    /**
     * Creates builder to build {@link SignResponseDto}.
     *
     * @return created builder
     */
    public static Builder builder()
    {
        return new Builder();
    }

    /**
     * Builder to build {@link SignResponseDto}.
     */
    public static final class Builder
    {

        private String ubl;
        private String hash;

        private Builder()
        {
        }

        public Builder withUbl(final String ubl)
        {
            this.ubl = ubl;
            return this;
        }

        public Builder withHash(final String hash)
        {
            this.hash = hash;
            return this;
        }

        public SignResponseDto build()
        {
            return new SignResponseDto(this);
        }
    }
}
