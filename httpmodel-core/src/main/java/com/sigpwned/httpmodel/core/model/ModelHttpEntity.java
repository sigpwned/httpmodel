package com.sigpwned.httpmodel.core.model;

import com.sigpwned.httpmodel.core.io.EntityInputStream;

/**
 * Standalone HTTP entity without built-in lifecycle controls. Good fit for entities that don't
 * require lifecycle management (e.g., in-memory entities), or entities with externally-managed
 * lifecycle (e.g., temporary files deleted on exit).
 */
public abstract class ModelHttpEntity {
  /**
   * Prefer for underlying stream to be bufferable.
   */
  public abstract EntityInputStream toEntityInputStream();
}
