package com.xy.shuhua.ui.message;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.xy.shuhua.R;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by xiaoyu on 2016/3/29.
 */
public class RongYunMessageFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rongyun_message_fragment, null);
        enterFragment(view);
        return view;
    }

    /**
     * ���� �Ự�б� ConversationListFragment
     */
    private void enterFragment(View view) {
        ConversationListFragment fragment = (ConversationListFragment) getChildFragmentManager().findFragmentById(R.id.conversationlist);
        Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //����˽�ĻỰ�Ǿۺ���ʾ
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//����Ⱥ��Ự�ۺ���ʾ
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//����������Ự�Ǿۺ���ʾ
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//����ϵͳ�Ự�Ǿۺ���ʾ
                .build();
        fragment.setUri(uri);
    }
}
