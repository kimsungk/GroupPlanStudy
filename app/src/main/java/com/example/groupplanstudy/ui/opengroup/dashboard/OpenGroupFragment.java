package com.example.groupplanstudy.ui.opengroup.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.groupplanstudy.databinding.FragmentOpengroupBinding;

public class OpenGroupFragment extends Fragment {

    private FragmentOpengroupBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        OpenGroupViewModel openGroupViewModel =
                new ViewModelProvider(this).get(OpenGroupViewModel.class);

        binding = FragmentOpengroupBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textOpengroup;
        openGroupViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}