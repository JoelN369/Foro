package com.joelnemi.foro.adapters;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.joelnemi.foro.fragments.FragmentAbout;
import com.joelnemi.foro.fragments.FragmentComments;
import com.joelnemi.foro.fragments.FragmentPosts;
import com.joelnemi.foro.listeners.IPerfilClickListener;
import com.joelnemi.foro.models.Usuario;

public class FragmentAdapter extends FragmentPagerAdapter {
    private int N_PAGES = 3;
    Context context;
    IPerfilClickListener listenerPerfil;
    Usuario user;

    public FragmentAdapter(FragmentManager fm, int behavior, Context context, IPerfilClickListener listenerPerfil, Usuario user) {
        super(fm, behavior);
        this.context = context;
        this.user = user;
        this.listenerPerfil = listenerPerfil;
    }

    @Override
    public Fragment getItem(int position) {


        switch (position) {
            case 0:
                FragmentPosts fragment = new FragmentPosts();
                Bundle args = new Bundle();
                args.putInt(FragmentPosts.ARG_FRAGMENT, position + 1);
                args.putSerializable("listener", listenerPerfil);
                args.putSerializable("usuario", user);
                fragment.setArguments(args);
                return fragment;
            case 1:
                FragmentComments fragment2 = new FragmentComments();
                Bundle args2 = new Bundle();
                args2.putInt(FragmentComments.ARG_FRAGMENT, position + 1);
                args2.putSerializable("usuario", user);
                fragment2.setArguments(args2);
                return fragment2;
            case 2:
                FragmentAbout fragment3 = new FragmentAbout();
                Bundle args3 = new Bundle();
                args3.putInt(FragmentAbout.ARG_FRAGMENT, position + 1);
                args3.putSerializable("usuario", user);
                fragment3.setArguments(args3);
                return fragment3;
            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        return N_PAGES;
    }
}
