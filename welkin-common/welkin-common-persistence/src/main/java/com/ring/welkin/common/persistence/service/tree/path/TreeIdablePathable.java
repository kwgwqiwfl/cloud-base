package com.ring.welkin.common.persistence.service.tree.path;

import com.ring.welkin.common.persistence.service.tree.id.TreeIdable;

import java.io.Serializable;

public interface TreeIdablePathable<ID extends Serializable, T extends TreeIdablePathable<ID, T>>
    extends TreeIdable<ID, T>, Pathable {
}
