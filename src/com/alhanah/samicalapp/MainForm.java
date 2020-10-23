/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alhanah.samicalapp;

import static com.alhanah.samicalapp.SamiApplication.*;
import com.alhanah.samicalapp.store.CityList;
import com.codename1.components.FloatingActionButton;
import com.codename1.l10n.L10NManager;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.Tabs;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import java.io.IOException;
import nasiiCalendar.BasicDate;
import nasiiCalendar.CalendarFactory;


/**
 *
 * @author DEll
 */
public class MainForm extends Form {

    Tabs tabs;
    Resources theme;
    static MainForm mainForm;
    public MainForm() {
        super("SamiCal V1.0 alhanah.com");
        mainForm=this;
        theme = UIManager.initFirstTheme("/theme");
        //changeLanguage();
        prepareMyLayout();
        
    }
    public static MainForm getForm(){
        return mainForm;
    }
    protected void addViewDate(ViewDate v) {
        int x = tabs.getSelectedIndex();
        tabs.insertTab(CalendarFactory.format(v.getMyModel().getSelectedDate()), null, v, x);
        tabs.revalidate();
        tabs.animateLayout(250);
    }

    void removeTab(Command aThis) {
        tabs.removeTabAt(tabs.getSelectedIndex());
        //tabs.revalidate();
        tabs.animateLayout(250);
    }

    CalendarView getCurrentController() {
        int x = tabs.getSelectedIndex();
        CalendarView dc = null;
        try {
            dc = (CalendarView) tabs.getSelectedComponent();
            return dc;
        } catch (IndexOutOfBoundsException e) {
            //hussam.println(e.getMessage());
            return null;
        }
    }

    public void closeCurrentTab() {
        int x = tabs.getSelectedIndex();
        Container dc = null;
        try {
            dc = (Container) tabs.getSelectedComponent();
        } catch (IndexOutOfBoundsException e) {
            //hussam.println(e.getMessage());
            return;
        }
        //hussam.println("x: " + x);
        if (tabs.getSelectedComponent() instanceof Container) {

            tabs.removeTabAt(x);
            tabs.revalidate();
            tabs.animateLayout(250);
        }
        if(tabs.getTabCount()==0){
            addMyTab(CalendarViewType.DAY_VIEW, CalendarFactory.getCurrentDate());
        }
    }

    protected CalendarView addMyTab(CalendarViewType ct, BasicDate bd) {
        CalendarView dc = new CalendarView(ct, bd);
        dc.addControllerListiner(new DateControllerListener() {
            @Override
            public void dateUpdated(Object source, BasicDate bd) {
                tabs.setTabTitle(dc.getHeader(), null, tabs.getSelectedIndex());
                tabs.revalidate();
                tabs.animate();
            }

        });
        int x = Math.max(0, tabs.getTabCount());
        tabs.insertTab(dc.getHeader(), null, dc, x);
        if(tabs.getTabCount()!=0)tabs.setSelectedIndex(x, true);
        tabs.revalidate();
        tabs.animate();
        //hussam.println("TAB UIID:"+tabs.getUIID());
    //    hussam.println("ToolBar: "+this.getToolbar()..getUIID());
        
        return dc;
    }
    protected void updateTabHeader(){
        CalendarView  c=(CalendarView) tabs.getSelectedComponent(); 
        tabs.setTabTitle(c.getHeader(), null, tabs.getSelectedIndex());
                tabs.revalidate();
                tabs.animate();
    }
    protected CalendarView setView(CalendarViewType ct, BasicDate bd) {
        CalendarView dc = (CalendarView) tabs.getSelectedComponent();
        
        dc.setView(ct, bd);
        
        dc.addControllerListiner(new DateControllerListener() {
            @Override
            public void dateUpdated(Object source, BasicDate bd) {
                updateTabHeader();
                /*tabs.setTabTitle(dc.getHeader(), null, tabs.getSelectedIndex());
                tabs.revalidate();
                tabs.animate();//*/
            }
        });
        updateTabHeader();
        int x = Math.max(0, tabs.getTabCount());
        /*    tabs.insertTab(dc.getHeader(), null, dc, x);
        tabs.setSelectedIndex(x, true);//*/
        tabs.revalidate();
        tabs.animate();
        //updateTabHeader();
        return dc;
    }

    void prepareMyLayout() {
        Form mainForm = this;
        mainForm.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        mainForm.setLayout(new BorderLayout());

        mainForm.getToolbar().setGlobalToolbar(true);
        //current.add(new DayBagCountainer(om.getDate(Calendar.getInstance().getTime().getTime())));
        final CalendarView cont1 = new CalendarView(CalendarViewType.DAY_VIEW, CalendarFactory.getCurrentDate());
        //  CalendarView cont2 = new CalendarView();
        //  CalendarView cont3 = new CalendarView(CalendarModel.CalType.MONTH);
        tabs = new Tabs();
        addMyTab(CalendarViewType.DAY_VIEW, CalendarFactory.getCurrentDate());
        /*tabs.addTab("Help", browser);
        tabs.getTabsContainer().add(new Button(new Command("+Y") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                addMyTab(CalendarModel.CalType.YEAR);

                //tabs.addTab(dc.getHeader(), dc);
            }

        }));
        tabs.getTabsContainer().add(new Button(new Command("+M") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                addMyTab(CalendarModel.CalType.MONTH);

                //tabs.addTab(dc.getHeader(), dc);
            }

        }));
        //*/

        cont1.addControllerListiner(new DateControllerListener() {
            @Override
            public void dateUpdated(Object source, BasicDate bd) {
                CalendarView dc = (CalendarView) tabs.getSelectedComponent();

                tabs.setTabTitle(dc.getHeader(), null, 0);
                tabs.revalidate();
                tabs.animate();
            }
        });
        mainForm.add(BorderLayout.CENTER, tabs);

        Label separator = new Label(" ");
        Style separatorStyle = separator.getAllStyles();
        separatorStyle.setBgImage(Image.createImage(40, 2, 0x7f000000));
        separatorStyle.setBackgroundType(Style.BACKGROUND_IMAGE_TILE_HORIZONTAL_ALIGN_CENTER);
        separatorStyle.setMargin(0, 0, 0, 0);
        mainForm.getToolbar().addComponentToSideMenu(separator);
        
        FontImage m = FontImage.createMaterial(FontImage.MATERIAL_APPS, mainForm.getStyle());
        mainForm.getToolbar().addCommandToLeftSideMenu("purchases", m, (e) -> {
            showBasket(null);
        });
        m = FontImage.createMaterial(FontImage.MATERIAL_LANGUAGE, mainForm.getStyle());
        mainForm.getToolbar().addCommandToLeftSideMenu("changeLang", m, (e) -> {
            SamiApplication.changeLanguage();
            updateView();
            //getCurrentController().();
       //     MainForm m1 = new MainForm();
    //    SamiApplication.app.mainForm = m1;
    //    m1.show();

        });
        m = FontImage.createMaterial(FontImage.MATERIAL_INFO, mainForm.getStyle());

        mainForm.getToolbar().addCommandToLeftSideMenu("about", m, (e) -> {

        });
        m = FontImage.createMaterial(FontImage.MATERIAL_HELP, mainForm.getStyle());

        mainForm.getToolbar().addCommandToLeftSideMenu("help", m, (e) -> {
            showHelp("hanahurl");
        //    SamiApplication.app.helpForm.show();
        });
        
        mainForm.getToolbar().addCommandToLeftSideMenu("switchMusic", m, (e) -> {
            if(SamiApplication.isMusicPlaying()){
                SamiApplication.stopMusic();
            }else{
                SamiApplication.playMusic();
            }
        });
        mainForm.getToolbar().addCommandToLeftSideMenu("selectcity", m, (e) -> {
            prepareCitySearch();
        
        });
        TextField text = new TextField(10);
        text.putClientProperty("GoButton", Boolean.TRUE);
        text.setHint("Enter YearDate");
        text.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                //hussam.println("args: " + arg0);
                text.stopEditing();
                int date = 0;
                try {

                    date = Integer.parseInt(text.getText());
                } catch (NumberFormatException e) {
                    //hussam.println("Ignore");
                    e.printStackTrace();
                    return;
                }
                CalendarView dc = (CalendarView) tabs.getSelectedComponent();
                BasicDate b = dc.getSelectedDate();
                dc.setSelectedDate(b.getCalendar().getDate(date, b.getMonth(), b.getDay()));
                tabs.setTabTitle(dc.getHeader(), null, tabs.getSelectedIndex());
            }
        });
        mainForm.getToolbar().setTitleComponent(text);
        //prepareFloatingButton();
    }

    protected void showCalenderChooser() {
        CalendarView d = getCurrentController();
        if (d == null) {
            return;
        }

        CalendarChooserDialog cc = new CalendarChooserDialog(d);
        cc.show();
    }
    protected void proposeBuy(BasicDate b){
        boolean choice = Dialog.show(getT(b.getCalendar().getName())+" "+getT("climitReached"), "calendarLimitReachedMessage", "showpacks", "cancel");
        if(choice){
            
            getForm().showBasket(b.getCalendar().getName());
        }
    }
    protected Container prepareFloatingButton(Container c) {
        //FloatingActionButton fab = FloatingActionButton.createFAB(FontImage.MATERIAL_ADD);
        FloatingActionButton fab = FloatingActionButton.createFAB(FontImage.MATERIAL_ADD);
        FloatingActionButton fab1 = fab.createSubFAB(FontImage.MATERIAL_CALENDAR_TODAY, "Configure Calendars");
        fab1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                showCalenderChooser();
            }
        });
        
        FloatingActionButton fab4 = fab.createSubFAB(FontImage.MATERIAL_TIMER, "New Page");
        fab4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                addMyTab(CalendarViewType.DAY_VIEW, getCurrentController().getSelectedDate());
                //addViewDate(new ViewDate(CalendarFactory.getSamiCalendar().getDate(new Date().getTime())));
                animate();

                //tabs.addTab(dc.getHeader(), dc);
            }

        });
        FloatingActionButton fab2 = fab.createSubFAB(FontImage.MATERIAL_DELETE, "Close");
        fab2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                closeCurrentTab();
            }
        });
        fab.getAllStyles().setBgColor(0x000000);
        fab1.getAllStyles().setBgColor(0x000000);
        fab2.getAllStyles().setBgColor(0x000000);
        fab4.getAllStyles().setBgColor(0x000000);
        
        return fab.bindFabToContainer(c, Component.RIGHT, Component.BOTTOM);

    }
    
    public static boolean getFormRTL(){
        return L10NManager.getInstance().getLanguage().equals("ar");
        
    }
    public void showBasket(String sku){
        BasketForm b = new BasketForm(this);
        b.showSKU(sku);
        b.show();
    }
    public void updateView(){
        for(int i=0;i<tabs.getTabCount();i++){
                CalendarView c=(CalendarView) tabs.getTabComponentAt(i);
                c.setView(c.getView(), c.getSelectedDate());
            }
            revalidate();
            animate();
    }
    public void prepareCitySearch(){
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                    if(SamiApplication.cityList==null){
                        System.err.println("strange city list is null");
                        SamiApplication.cityList=new CityList();
                    }
                    CitySearchForm form=new CitySearchForm(SamiApplication.cityList, SamiApplication.getBasicLocation());
                    
                Display.getInstance().callSerially(new Runnable() {
                    @Override
                    public void run() {
                        form.show();
                    }
                });
               
            }   
        });
        t.start();
        
    }
}
