package com.group12.service_app.core.repositories.interfaces;

import com.google.firebase.database.DataSnapshot;
import com.group12.service_app.data.models.Listing;

/**
 * Created by james on 3/4/18.
 */

public interface IListingReader {
    void onNewListing(Listing listing);
    void onListingModified(Listing listing);
    void onListingRemoved(Listing listing);
    void onListingMoved(Listing listing);
}
