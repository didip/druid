/*
 * Druid - a distributed column store.
 * Copyright (C) 2012, 2013  Metamarkets Group Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package com.metamx.druid.indexing.coordinator.scaling;

import com.google.inject.Inject;
import com.metamx.common.concurrent.ScheduledExecutorFactory;
import com.metamx.druid.indexing.coordinator.RemoteTaskRunner;

/**
 */
public class ResourceManagementSchedulerFactoryImpl implements ResourceManagementSchedulerFactory
{
  private final ResourceManagementSchedulerConfig config;
  private final ResourceManagementStrategy strategy;
  private final ScheduledExecutorFactory executorFactory;

  @Inject
  public ResourceManagementSchedulerFactoryImpl(
      ResourceManagementStrategy strategy,
      ResourceManagementSchedulerConfig config,
      ScheduledExecutorFactory executorFactory
  )
  {
    this.config = config;
    this.strategy = strategy;
    this.executorFactory = executorFactory;
  }

  @Override
  public ResourceManagementScheduler build(RemoteTaskRunner runner)
  {
    if (config.isDoAutoscale()) {
      return new ResourceManagementScheduler(runner, strategy, config, executorFactory.create(1, "ScalingExec--%d"));
    }
    else {
      return new NoopResourceManagementScheduler();
    }
  }
}
