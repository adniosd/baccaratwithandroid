package com.vivogaming.livecasino.screens.topmenu;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.vivogaming.livecasino.R;
import com.vivogaming.livecasino.screens.game.Game;

import static com.vivogaming.livecasino.screens.register.Register.showToastRegister;
import static com.vivogaming.livecasino.screens.register.ValidateDate.EMAIL_ADDRESS_PATTERN;

/**
 * User: Assasin
 * Date: 10.10.13
 * Time: 14:52
 */
public class Help extends Dialog implements View.OnClickListener {
    private  TabHost menuHelp;
    private static TextView textToast;
    private static Toast toast;

    public Help(final Context context)
    {
        // Set your theme here
        super(context, R.style.MenuContentThemes);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.dialog_menu_help);
        final double width = getContext().getResources().getDimension(R.dimen.menu_content_width);
        final double height = getContext().getResources().getDisplayMetrics().heightPixels * 0.9;
        getWindow().setLayout((int) Math.round(width), (int) Math.round(height));

        // get TabHost
        menuHelp = (TabHost) findViewById(R.id.tabHost);
        menuHelp.setup();

        //setupTab(компонент в якому міститься текст ячейок меню,
        // сам текст,
        // контент до відповідної вкладки,
        // селектор до відповідної вкладки,
        // )

        setupTab(new TextView(getContext()), "Rules", R.id.tabRules, R.drawable.background_dialog_help_tab_rules_selector);
        setupTab(new TextView(getContext()), "About As", R.id.tabAboutAs, R.drawable.background_dialog_help_tab_about_selector);
        setupTab(new TextView(getContext()), "Contact", R.id.tabContact, R.drawable.background_dialog_help_tab_contact_selector);

        findViewById(R.id.btnCancelForHelp_SMH).setOnClickListener(this);
        findViewById(R.id.btSend_DHM).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findElem();
            }
        });
    }

    public void onClick(View v) {
        dismiss();
    }

    private void setupTab(final View _view, final String _tag, final int _idContent, final int _idSelector) {
        View tabview = createTabView(menuHelp.getContext(), _tag, _idSelector);
        TabHost.TabSpec setContentMenu = menuHelp.newTabSpec(_tag).setIndicator(tabview).setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {return _view;}
        });
        setContentMenu.setContent(_idContent);
        menuHelp.addTab(setContentMenu);
    }

    private static View createTabView(final Context _context, final String _text, final int _idSelector) {
        View view = LayoutInflater.from(_context).inflate(R.layout.tab_menu_content, null);
        TextView tvNameVkl = (TextView) view.findViewById(R.id.tvContent);
        LinearLayout llVkl = (LinearLayout) view.findViewById(R.id.llNameMenuHelp);
        llVkl.setBackgroundResource(_idSelector);
        tvNameVkl.setTextSize(15);
        tvNameVkl.setText(_text);
        return view;
    }

    private void findElem() {
        EditText nameEt = (EditText) findViewById(R.id.etContact);
        EditText emailEt = (EditText) findViewById(R.id.etContactEmail);
        EditText messageEt = (EditText) findViewById(R.id.etContactMessage);
        String name = nameEt.toString();
        String email = emailEt.getText().toString();
        String message = messageEt.getText().toString();

        if(!checkEmail(email)){
            showToastRegister(Game.getGameActivity(),R.string.valid_email);
        } else {
            if(message.equals("") || name.equals("") || email.equals("")){
             showToastRegister(Game.getGameActivity(),R.string.fill_all);
            }else {
                showToastRegister(Game.getGameActivity(),R.string.send);
                clearFields(nameEt,emailEt,messageEt);
        dismiss();
            }
        }
    }

    private void clearFields(EditText _nameEt, EditText _emailEt, EditText _message){
        _nameEt.setText("");
        _emailEt.setText("");
        _message.setText("");
    }

    private static boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }
}
