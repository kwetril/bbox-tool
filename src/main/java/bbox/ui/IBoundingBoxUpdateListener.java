package bbox.ui;

import bbox.model.BoundingBox;

public interface IBoundingBoxUpdateListener {
    void boxCreated(BoundingBox box);
    void boxUpdated(BoundingBox box);
}
