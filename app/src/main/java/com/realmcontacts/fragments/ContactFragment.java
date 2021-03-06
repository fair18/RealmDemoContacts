package com.realmcontacts.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.google.common.base.Optional;
import com.realmcontacts.R;
import com.realmcontacts.adapters.ContactsRecyclerAdapter;
import com.realmcontacts.model.Contact;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Yuriy on 2016-04-14 RealmContacts.
 */
public class ContactFragment extends BaseFragment {

    private static final String TAG = "ContactFragment";

    @Bind(R.id.contact_realm_recycler_view)
    RealmRecyclerView realmRecyclerView;

    private Optional<Realm> realm;
    private ContactsRecyclerAdapter contactAdapter;

    @Override
    protected int getLayout() {
        return R.layout.fragment_tab_contact;
    }

    public ContactFragment() {
    }

    public static ContactFragment newInstance() {
        return new ContactFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupListView();
    }

    private void setupListView() {
        try {
            if (isActivityNotNull()) {
                realm = Optional.of(Realm.getDefaultInstance());
                if (realm.isPresent()) {
                    RealmResults<Contact> realmResults = realm.get()
                            .where(Contact.class)
                            .isNotNull("vsrName")
                            .findAllSorted("vsrName", Sort.ASCENDING);

                    contactAdapter = new ContactsRecyclerAdapter(getActivity(), realmResults, true, true, true, "vsrName");
                    realmRecyclerView.setAdapter(contactAdapter);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (contactAdapter != null) contactAdapter.close();
        if (realm.isPresent()) realm.get().close();
        realm = Optional.absent();
        if (isActivityNotNull()) Picasso.with(getActivity()).cancelTag(this);
    }

}
