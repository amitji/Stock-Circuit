package com.abile2.stockcircuit.util;

import java.util.ArrayList;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.abile2.stockcircuit.model.NewsFeedItem;


public class RssHandler extends DefaultHandler{
    private ArrayList<NewsFeedItem> messages;
    private NewsFeedItem currentMessage;
    private StringBuilder builder = new StringBuilder();
    private static final String TAG_ITEM = "item";
    
    private final Stack<String> tagsStack = new Stack<String>();
    
    public ArrayList<NewsFeedItem> getMessages(){
        return this.messages;
    }
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        super.characters(ch, start, length);
        builder.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        super.endElement(uri, localName, name);
        
        String tag = peekTag();
        if (tag !=null && !name.equals(tag)) {
            throw new InternalError();
        }

        popTag();
        String parentTag = peekTag();

        
        
        if (this.currentMessage != null){
            if (localName.equalsIgnoreCase("title")){
            	if (TAG_ITEM.equalsIgnoreCase(parentTag)) {
            		currentMessage.setTitle(builder.toString());
                } 
                
            } else if (localName.equalsIgnoreCase("link")){
            	if (TAG_ITEM.equalsIgnoreCase(parentTag)) {
                    currentMessage.setLink(builder.toString());
                } 
            } else if (localName.equalsIgnoreCase("description")){
               	if (TAG_ITEM.equalsIgnoreCase(parentTag)) {
                    currentMessage.setDescription(builder.toString());
                } 
            } else if (localName.equalsIgnoreCase("item")){
                messages.add(currentMessage);
            }
            
        }
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        pushTag("");
        messages = new ArrayList<NewsFeedItem>();
        builder = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, name, attributes);
        builder.setLength(0);    
        pushTag(name);
        if (localName.equalsIgnoreCase("item")){
            this.currentMessage = new NewsFeedItem();
        }
    }
    private void pushTag(String tag) {
        tagsStack.push(tag);
    }

    private String popTag() {
        return tagsStack.pop();
    }

    private String peekTag() {
    	//if(tagsStack.size() > 0)
    		return tagsStack.peek();
    	//else  return null;
    }
}
