package de.juplo.plugins.hibernate;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.Map;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.schema.SourceType;
import org.hibernate.tool.schema.spi.ContributableMatcher;
import org.hibernate.tool.schema.spi.ExecutionOptions;
import org.hibernate.tool.schema.spi.SchemaManagementTool;
import org.hibernate.tool.schema.spi.ScriptSourceInput;
import org.hibernate.tool.schema.spi.SourceDescriptor;
import org.hibernate.tool.schema.spi.TargetDescriptor;


/**
 * Generate/Execute SQL to drop all tables of a database-schema that represents
 * the configured mappings.
 *
 * @goal drop
 * @phase process-classes
 * @threadSafe
 * @requiresDependencyResolution runtime
 */
public class DropMojo extends AbstractSchemaMojo
{
  /**
   * Output file.
   * <p>
   * If the specified filename is not absolut, the file will be created
   * relative to the project build directory
   * (<code>project.build.directory</code>).
   *
   * @parameter property="hibernate.schema.drop" default-value="drop.sql"
   * @since 1.0
   */
  private String outputFile;


  @Override
  public final void execute()
    throws
      MojoFailureException,
      MojoExecutionException
  {
    super.execute(outputFile);
  }


  @Override
  void build(
      MetadataImplementor metadata,
      ExecutionOptions options,
      TargetDescriptor target
      )
      throws
        MojoExecutionException,
        MojoFailureException
  {
    ServiceRegistry service =
        metadata.getMetadataBuildingOptions().getServiceRegistry();
    SchemaManagementTool tool = service.getService(SchemaManagementTool.class);

    Map config = options.getConfigurationValues();
    SourceDescriptor source = new SourceDescriptor()
    {
	  @Override
      public SourceType getSourceType()
      {
        return SourceType.METADATA;
      }

      @Override
      public ScriptSourceInput getScriptSourceInput()
      {
        return null;
      }
    };

    ContributableMatcher contributableMatcher = ContributableMatcher.ALL;
    
    tool.getSchemaDropper(config).doDrop(metadata, options, contributableMatcher, source, target);
  }
}
