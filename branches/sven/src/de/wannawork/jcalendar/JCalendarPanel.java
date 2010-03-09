package de.wannawork.jcalendar;
/*
 * Copyright (c) 2003, Bodo Tasche (http://www.wannawork.de)
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice, this 
 *       list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright notice, this 
 *       list of conditions and the following disclaimer in the documentation and/or other 
 *       materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS 
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE 
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED 
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This JCalendarPanel shows a Calendar.
 * 
 * It is coded with 2 rules:
 * <ul>
 * <li>No hard coded Fonts or Colors, use the current Look and Feel</li>
 * <li>No hard coded locale behaviour, use the given Locale (Start of Week, Name of Days/Months)</li>
 * </ul>
 *
 * You can add a ChangeListener to this JCalendarPanel to receive 
 * change events.
 *  
 * @author Bodo Tasche, Scott Sirovy
 */
public class JCalendarPanel extends JPanel implements ItemListener, ChangeListener {

	/**
	 * Creates a Calendar using the current Date and 
	 * current Local settings.
	 */
	public JCalendarPanel() {
		createGUI(Calendar.getInstance(), Locale.getDefault(), DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault()), true);
	}

	/**
	 * Creates a Calendar using the cal-Date and 
	 * current Locale Settings. It doesn't use
	 * the Locale in the Calendar-Object !
	 * 
	 * @param cal Calendar to use
	 */
	public JCalendarPanel(Calendar cal) {
		createGUI(cal, Locale.getDefault(), DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault()), true);
	}

	/**
	 * Creates a Calendar using the current Date
	 * and the given Locale Settings.
	 * 
	 * @param locale Locale to use
	 */
	public JCalendarPanel(Locale locale) {
		createGUI(Calendar.getInstance(locale), locale, DateFormat.getDateInstance(DateFormat.MEDIUM, locale), true);
	}

	/**
	 * Creates a Calender using the given Date and
	 * Locale 
	 * 
	 * @param cal Calendar to use
	 * @param locale Locale to use
	 */
	public JCalendarPanel(Calendar cal, Locale locale) {
		createGUI(cal, locale, DateFormat.getDateInstance(DateFormat.MEDIUM, locale), true);
	}

	/**
	 * Creates a Calender using the given Calendar,
	 * Locale and DateFormat.
	 * 
	 * @param cal Calendar to use
	 * @param locale Locale to use
	 * @param dateFormat DateFormat for the ComboBox
	 */
	public JCalendarPanel(Calendar cal, Locale locale, DateFormat dateFormat) {
		createGUI(cal, locale, dateFormat, true);
	}

	/**
	 * Creates a Calender using the given Calendar,
	 * Locale and DateFormat.
	 * 
	 * @param cal Calendar to use
	 * @param locale Locale to use
	 * @param dateFormat DateFormat for the ComboBox
	 * @param flat Flat Buttons for Navigation at the Bottom ?
	 */
	public JCalendarPanel(Calendar cal, Locale locale, DateFormat dateFormat, boolean flat) {
		createGUI(cal, locale, dateFormat, flat);
	}

	/**
	 * Creates the GUI
	 * 
	 * @param cal Calendar to use
	 * @param locale Locale to use
	 * @param dateFormat DateFormat to use
	 * @param flat Flat Buttons for Navigation at the Bottom ?
	 */
	private void createGUI(Calendar cal, Locale locale, DateFormat dateFormat, boolean flat) {
		_locale = locale;
		_cal = Calendar.getInstance(locale);
		_cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
		_cal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
		_cal.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		_format = dateFormat;

		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		_month = createMonth();
		_month.addItemListener(this);
		add(_month, c);

		_year = createYear();
		_year.addItemListener(this);

		c.gridwidth = GridBagConstraints.REMAINDER;

		add(_year, c);

		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 1, 1, 1);

		_monthPanel = new JMonthPanel(_cal, _locale);
		_monthPanel.addChangeListener(this);

		add(_monthPanel, c);

		c.insets = new Insets(0, 0, 1, 0);
		add(createButtonPanel(flat), c);
		
		_monthPanel.grabFocus();
	}

	/**
	 * Creates the ButtonPanel on the bottom
	 * @param flat Flat Buttons for Navigation at the Bottom ?
	 * @return JPanel with Buttons
	 */
	private JPanel createButtonPanel(boolean flat) {
		JPanel buttonpanel = new JPanel();

		JButton yearLeft;
		JButton dayLeft;
		JButton today;
		JButton dayRight;
		JButton yearRight;

		if (flat) {
			yearLeft = new FlatButton("<<"); //$NON-NLS-1$
			dayLeft = new FlatButton("<"); //$NON-NLS-1$
			today = new FlatButton(LocaleStrings.getString("JCalendarPanel.Today")); //$NON-NLS-1$
			dayRight = new FlatButton(">"); //$NON-NLS-1$
			yearRight = new FlatButton(">>"); //$NON-NLS-1$
		} else {
			yearLeft = new JButton("<<"); //$NON-NLS-1$
			yearLeft.setMargin(new Insets(1, 1, 1, 1));
			dayLeft = new JButton("<"); //$NON-NLS-1$
			dayLeft.setMargin(new Insets(1, 1, 1, 1));
			today = new JButton(LocaleStrings.getString("JCalendarPanel.Today")); //$NON-NLS-1$
			today.setMargin(new Insets(2, 2, 2, 2));
			dayRight = new JButton(">"); //$NON-NLS-1$
			dayRight.setMargin(new Insets(1, 1, 1, 1));
			yearRight = new JButton(">>"); //$NON-NLS-1$
			yearRight.setMargin(new Insets(1, 1, 1, 1));
		}


		buttonpanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(0, 0, 0, 5);

		yearLeft.setMargin(new Insets(1, 1, 1, 1));
		yearLeft.setToolTipText(LocaleStrings.getString("JCalendarPanel.Last_Year")); //$NON-NLS-1$
		yearLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (_year.getSelectedIndex() > 0) {
				    
				    int month = _cal.get(Calendar.MONTH);
					_cal.set(Calendar.YEAR,_cal.get(Calendar.YEAR) - 1);
					if (_cal.get(Calendar.MONTH) != month) {
					    _cal.set(Calendar.MONTH, month);
					}
					setCalendar(_cal);
				}
			}
		});
		buttonpanel.add(yearLeft, c);

		dayLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                int monthIndex = _cal.get(Calendar.MONTH);
                _cal.set(Calendar.MONTH, monthIndex - 1);
                if (_cal.get(Calendar.MONTH) == monthIndex) {
                    _cal.set(Calendar.DAY_OF_MONTH, 0);
                }
				setCalendar(_cal);
			}
		});

		dayLeft.setMargin(new Insets(1, 1, 1, 1));
		dayLeft.setToolTipText(LocaleStrings.getString("JCalendarPanel.Last_Month")); //$NON-NLS-1$

		buttonpanel.add(dayLeft, c);


		GridBagConstraints c2 = new GridBagConstraints();
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.weightx = 1.0;
		
		today.setMargin(new Insets(2, 2, 2, 2));
		today.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCalendar(Calendar.getInstance());
			}
		});
		buttonpanel.add(today, c2);

		c.insets = new Insets(0, 5, 0, 0);

		dayRight.setMargin(new Insets(1, 1, 1, 1));
		dayRight.setToolTipText(LocaleStrings.getString("JCalendarPanel.Next_Month")); //$NON-NLS-1$
		dayRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                int monthIndex = _cal.get(Calendar.MONTH);
				_cal.set(Calendar.MONTH, monthIndex + 1);
                if (_cal.get(Calendar.MONTH) != (monthIndex + 1 ) % 12) {
                    _cal.set(Calendar.DAY_OF_MONTH, 0);
                }
				setCalendar(_cal);
			}
		});		
		buttonpanel.add(dayRight, c);

		yearRight.setMargin(new Insets(1, 1, 1, 1));
		yearRight.setToolTipText(LocaleStrings.getString("JCalendarPanel.Next_Year")); //$NON-NLS-1$
		yearRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (_year.getSelectedIndex() < _year.getItemCount() - 1) {
				    int month = _cal.get(Calendar.MONTH);
					_cal.set(Calendar.YEAR,_cal.get(Calendar.YEAR) + 1);
					if (_cal.get(Calendar.MONTH) != month) {
					    _cal.set(Calendar.MONTH, month);
					}
					
					setCalendar(_cal);
				}
			}
		});
		buttonpanel.add(yearRight, c);

		return buttonpanel;
	}

	/**
	 * Creates a JComboBox filled with year values (1900-2100)
	 * @return JComboBox with Years
	 */
	private JComboBox createYear() {
		JComboBox year = new JComboBox();

		for (int i = 1900; i <= 2100; i++) {
			year.addItem("" + i); //$NON-NLS-1$
		}

		year.setSelectedIndex(_cal.get(Calendar.YEAR) - 1900);

		return year;
	}

	/**
	 * Creates a JComboBox filled with Months.
	 * The name for the Month is created using the locale given
	 * in the constructor.
	 * 
	 * @return JComboBox filled with Months
	 */
	private JComboBox createMonth() {
		JComboBox month = new JComboBox();

		SimpleDateFormat format = new SimpleDateFormat("MMMMM", _locale);

//		 Probably should be using the same locale as the rest of the widget.
		Calendar currentCal = Calendar.getInstance(_locale);
//		 Setting the day to 1 will avoids problems with leap years.
//		 If the calendar is created on March 30, 2004 (like I just did)
//		 then the day defaults to 30.  When the month is set to 1 (February)  
//		 the currentCal will *adjust* it to become March 1.  This results
//		 in TWO entries with the value of \"March\" in the month combo.
		currentCal.set(Calendar.DAY_OF_MONTH, 1);
		
		for (int i = 0; i < 12; i++) {
			currentCal.set(Calendar.MONTH, i);
			currentCal.set(Calendar.YEAR, _cal.get(Calendar.YEAR));
			String myString = format.format(currentCal.getTime());
			month.addItem(myString);
		}

		month.setSelectedIndex(_cal.get(Calendar.MONTH));

		return month;
	}

	/**
	 * Updates the Calendar
	 */
	private void updateCalendar() {
		if (!_updating) {
			_updating = true;
			_cal.set(Calendar.MONTH, _month.getSelectedIndex());
			_cal.set(Calendar.YEAR, _year.getSelectedIndex() + 1900);
			_cal.set(Calendar.DAY_OF_MONTH, _monthPanel.getSelectedDayOfMonth());

			_monthPanel.setCalendar(_cal);
			_monthPanel.grabFocus();
			_updating = false;
		}
	}

	/**
	 * Returns the current selected Date as Calender-Object
	 * 
	 * @return current selected Date
	 */
	public Calendar getCalendar() {
		updateCalendar();
		return _cal;
	}

	/**
	 * Sets the current selected Date
	 * 
	 * @param cal the Date to select
	 */
	public void setCalendar(Calendar cal) {
		_updating = true;
		_cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
		_cal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
		_cal.set(Calendar.YEAR, cal.get(Calendar.YEAR));

		_monthPanel.setCalendar(_cal);
		_year.setSelectedIndex(_cal.get(Calendar.YEAR) - 1900);
		_month.setSelectedIndex(_cal.get(Calendar.MONTH));
		_monthPanel.grabFocus();
		_updating = false;
	}

	/**
	 * Returns a String-Representation of this Calendar using
	 * the DateFormat given in the Constructor
	 * 
	 * @return String-Representation of this Calendar
	 */
	public String toString() {
		updateCalendar();
		return _format.format(_cal.getTime());
	}

	/**
	 * Returns a String-Representation of this Calendar
	 * using the given DateFormat
	 * 
	 * @param format DateFormat to use
	 * @return String-Representation of this Calendar
	 */
	public String toString(DateFormat format) {
		updateCalendar();
		return format.format(_cal.getTime());
	}

	/** 
	 * Recieves StateChanges from the ComboBoxes for Month/Year
	 * and updates the Calendar
	 * 
	 * @param e ItemEvent
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	public void itemStateChanged(ItemEvent e) {
		updateCalendar();
		if (_listenermode == FIRE_EVERYTIME) {
			fireChangeEvent();
		}
	}

	/**
	 * Recieves StateChanges from the MonthPanel and
	 * updates the Calendar
	 * 
	 * @param e ChangeEvent
	 * 
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	public void stateChanged(ChangeEvent e) {
		updateCalendar();
		fireChangeEvent();
	}

	/**
	 * Adds a Changelistener to this JCalendarPanel.
	 * 
	 * It will be called every time the selected Date
	 * changes.
	 * 
	 * @param listener ChangeListener
	 */
	public void addChangeListener(ChangeListener listener) {
		_changeListener.add(listener);
	}

	/**
	 * Removes a ChangeListener from this JCalendarPanel
	 * 
	 * @param listener listener to remove
	 */
	public void removeChangeListener(ChangeListener listener) {
		_changeListener.remove(listener);
	}

	/**
	 * Gets all ChangeListeners
	 * @return all ChangeListeners
	 */
	public ChangeListener[] getChangeListener() {
		return (ChangeListener[]) _changeListener.toArray();
	}

	/**
	 * Fires the ChangeEvent
	 */
	protected void fireChangeEvent() {
		if (!_fireingChangeEvent) {
			_fireingChangeEvent = true;
			ChangeEvent event = new ChangeEvent(this);

			for (int i = 0; i < _changeListener.size(); i++) {
				((ChangeListener) _changeListener.get(i)).stateChanged(event);
			}

			_fireingChangeEvent = false;
		}

	}

	/**
	 * Sets the Mode when the FireChangeEvent is called.
	 * Use FIRE_EVERYTIME or FIRE_DAYCHANGES as parameter.
	 * 
	 * @param mode The Mode of the Listener
	 */
	public void setListenerModus(int mode) {
		_listenermode = mode;
	}

	/**
	 * Enables/Disables the Panel
	 * @param enabled Enabled ?
	 */
	public void setEnabled(boolean enabled) {
		_month.setEnabled(enabled);
		_year.setEnabled(enabled);
		_monthPanel.setEnabled(enabled);
	}

	/**
	 * Is the Panel enabled ?
	 * @return enabled ?
	 */
	public boolean isEnabled() {
		return _month.isEnabled();
	}

	/**
	 * Returns the Dateformat the Panel is using
	 * @return DateFormat
	 */
	public DateFormat getDateFormat() {
	    return _format;
	}
	
	/**
	 * Fires everytime the Date changes
	 */
	public static final int FIRE_EVERYTIME = 1;
	/**
	 * Fires only if the Day changes
	 */
	public static final int FIRE_DAYCHANGES = 2;

	/**
	 * When does FireEvent() fire events?
	 * Every time there is an update or only
	 * if the Day was changed?
	 */
	private int _listenermode = FIRE_EVERYTIME;

	/**
	 * Current chang in progress?
	 */
	private boolean _updating = false;

	/**
	 * The current Date
	 */
	private Calendar _cal;
	/**
	 * The DateFormat for Output
	 */
	private DateFormat _format;
	/**
	 * The Locale to use
	 */
	private Locale _locale;

	/**
	 * The JComboBox for Month-Selection
	 */
	private JComboBox _month;
	/**
	 * The JComboBox for Year-Selection
	 */
	private JComboBox _year;
	/**
	 * The JMonthPanel for Day-Selection
	 */
	private JMonthPanel _monthPanel;

	/**
	 * The list of ChangeListeners
	 */
	private ArrayList _changeListener = new ArrayList();
	/**
	 * Currently firing an ChangeEvent?
	 */
	private boolean _fireingChangeEvent = false;
}