/*
 * Copyright 2017-2020 The OpenTracing Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package io.opentracing.contrib.redis.jedis3;

import io.opentracing.contrib.redis.common.TracingConfiguration;
import java.util.Set;
import java.util.function.Function;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.TracingJedisWrapper;

public class TracingJedisSentinelPool extends JedisSentinelPool {

  private final TracingConfiguration tracingConfiguration;

  public TracingJedisSentinelPool(TracingConfiguration tracingConfiguration, String masterName,
      Set<String> sentinels,
      final GenericObjectPoolConfig poolConfig) {
    super(masterName, sentinels, poolConfig, Protocol.DEFAULT_TIMEOUT, null,
        Protocol.DEFAULT_DATABASE);
    this.tracingConfiguration = tracingConfiguration;
  }

  public TracingJedisSentinelPool(TracingConfiguration tracingConfiguration, String masterName,
      Set<String> sentinels) {
    super(masterName, sentinels, new GenericObjectPoolConfig(), Protocol.DEFAULT_TIMEOUT, null,
        Protocol.DEFAULT_DATABASE);
    this.tracingConfiguration = tracingConfiguration;
  }

  public TracingJedisSentinelPool(TracingConfiguration tracingConfiguration, String masterName,
      Set<String> sentinels, String password) {
    super(masterName, sentinels, new GenericObjectPoolConfig(), Protocol.DEFAULT_TIMEOUT, password);
    this.tracingConfiguration = tracingConfiguration;
  }

  public TracingJedisSentinelPool(TracingConfiguration tracingConfiguration, String masterName,
      Set<String> sentinels,
      final GenericObjectPoolConfig poolConfig, int timeout, final String password) {
    super(masterName, sentinels, poolConfig, timeout, password, Protocol.DEFAULT_DATABASE);
    this.tracingConfiguration = tracingConfiguration;
  }

  public TracingJedisSentinelPool(TracingConfiguration tracingConfiguration, String masterName,
      Set<String> sentinels,
      final GenericObjectPoolConfig poolConfig, final int timeout) {
    super(masterName, sentinels, poolConfig, timeout, null, Protocol.DEFAULT_DATABASE);
    this.tracingConfiguration = tracingConfiguration;
  }

  public TracingJedisSentinelPool(TracingConfiguration tracingConfiguration, String masterName,
      Set<String> sentinels,
      final GenericObjectPoolConfig poolConfig, final String password) {
    super(masterName, sentinels, poolConfig, Protocol.DEFAULT_TIMEOUT, password);
    this.tracingConfiguration = tracingConfiguration;
  }

  public TracingJedisSentinelPool(TracingConfiguration tracingConfiguration, String masterName,
      Set<String> sentinels,
      final GenericObjectPoolConfig poolConfig, int timeout, final String password,
      final int database) {
    super(masterName, sentinels, poolConfig, timeout, timeout, password, database);
    this.tracingConfiguration = tracingConfiguration;
  }

  public TracingJedisSentinelPool(TracingConfiguration tracingConfiguration, String masterName,
      Set<String> sentinels,
      final GenericObjectPoolConfig poolConfig, int timeout, final String password,
      final int database, Function<String, String> customSpanName) {
    super(masterName, sentinels, poolConfig, timeout, timeout, password, database);
    this.tracingConfiguration = tracingConfiguration;
  }

  public TracingJedisSentinelPool(TracingConfiguration tracingConfiguration, String masterName,
      Set<String> sentinels,
      final GenericObjectPoolConfig poolConfig, int timeout, final String password,
      final int database, final String clientName) {
    super(masterName, sentinels, poolConfig, timeout, timeout, password, database, clientName);
    this.tracingConfiguration = tracingConfiguration;
  }

  public TracingJedisSentinelPool(TracingConfiguration tracingConfiguration, String masterName,
      Set<String> sentinels,
      final GenericObjectPoolConfig poolConfig, final int timeout, final int soTimeout,
      final String password, final int database) {
    super(masterName, sentinels, poolConfig, timeout, soTimeout, password, database, null);
    this.tracingConfiguration = tracingConfiguration;
  }

  public TracingJedisSentinelPool(TracingConfiguration tracingConfiguration, String masterName,
      Set<String> sentinels,
      final GenericObjectPoolConfig poolConfig, final int connectionTimeout, final int soTimeout,
      final String password, final int database, final String clientName) {
    super(masterName, sentinels, poolConfig, connectionTimeout, soTimeout, password, database,
        clientName);
    this.tracingConfiguration = tracingConfiguration;
  }

  @Override
  public Jedis getResource() {
    Jedis resource = super.getResource();
    return new TracingJedisWrapper(resource, tracingConfiguration);
  }

  @Override
  @Deprecated
  public void returnBrokenResource(final Jedis resource) {
    super.returnBrokenResource(unwrapResource(resource));
  }

  @Override
  @Deprecated
  public void returnResource(final Jedis resource) {
    super.returnResource(unwrapResource(resource));
  }

  private Jedis unwrapResource(Jedis resource) {
    return (resource instanceof TracingJedisWrapper)
        ? ((TracingJedisWrapper) resource).getWrapped()
        : resource;
  }
}
