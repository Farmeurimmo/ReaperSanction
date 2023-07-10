package fr.farmeurimmo.reapersanction.utils;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.*;

public class DiscordWebhook {
    private final String url;
    private final List<EmbedObject> embeds;
    private String content;
    private String username;
    private String avatarUrl;
    private boolean tts;

    public DiscordWebhook(final String url) {
        this.embeds = new ArrayList<EmbedObject>();
        this.url = url;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public void setAvatarUrl(final String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setTts(final boolean tts) {
        this.tts = tts;
    }

    public void addEmbed(final EmbedObject embed) {
        this.embeds.add(embed);
    }

    public void execute() throws IOException {
        if (this.content == null && this.embeds.isEmpty()) {
            throw new IllegalArgumentException("Set content or add at least one EmbedObject");
        }
        final JSONObject json = new JSONObject();
        json.put("content", this.content);
        json.put("username", this.username);
        json.put("avatar_url", this.avatarUrl);
        json.put("tts", this.tts);
        if (!this.embeds.isEmpty()) {
            final List<JSONObject> embedObjects = new ArrayList<JSONObject>();
            for (final EmbedObject embed : this.embeds) {
                final JSONObject jsonEmbed = new JSONObject();
                jsonEmbed.put("title", embed.getTitle());
                jsonEmbed.put("description", embed.getDescription());
                jsonEmbed.put("url", embed.getUrl());
                if (embed.getColor() != null) {
                    final Color color = embed.getColor();
                    int rgb = color.getRed();
                    rgb = (rgb << 8) + color.getGreen();
                    rgb = (rgb << 8) + color.getBlue();
                    jsonEmbed.put("color", rgb);
                }
                final EmbedObject.Footer footer = embed.getFooter();
                final EmbedObject.Image image = embed.getImage();
                final EmbedObject.Thumbnail thumbnail = embed.getThumbnail();
                final EmbedObject.Author author = embed.getAuthor();
                final List<EmbedObject.Field> fields = embed.getFields();
                if (footer != null) {
                    final JSONObject jsonAuthor = new JSONObject();
                    jsonAuthor.put("text", footer.getText());
                    jsonAuthor.put("icon_url", footer.getIconUrl());
                    jsonEmbed.put("footer", jsonAuthor);
                }
                if (image != null) {
                    final JSONObject jsonAuthor = new JSONObject();
                    jsonAuthor.put("url", image.getUrl());
                    jsonEmbed.put("image", jsonAuthor);
                }
                if (thumbnail != null) {
                    final JSONObject jsonAuthor = new JSONObject();
                    jsonAuthor.put("url", thumbnail.getUrl());
                    jsonEmbed.put("thumbnail", jsonAuthor);
                }
                if (author != null) {
                    final JSONObject jsonAuthor = new JSONObject();
                    jsonAuthor.put("name", author.getName());
                    jsonAuthor.put("url", author.getUrl());
                    jsonAuthor.put("icon_url", author.getIconUrl());
                    jsonEmbed.put("author", jsonAuthor);
                }
                final List<JSONObject> jsonFields = new ArrayList<JSONObject>();
                for (final EmbedObject.Field field : fields) {
                    final JSONObject jsonField = new JSONObject();
                    jsonField.put("name", field.getName());
                    jsonField.put("value", field.getValue());
                    jsonField.put("inline", field.isInline());
                    jsonFields.add(jsonField);
                }
                jsonEmbed.put("fields", jsonFields.toArray());
                embedObjects.add(jsonEmbed);
            }
            json.put("embeds", embedObjects.toArray());
        }
        final URL url = new URL(this.url);
        final HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.addRequestProperty("Content-Type", "application/json");
        connection.addRequestProperty("User-Agent", "Java-DiscordWebhook-BY-Gelox_");
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        final OutputStream stream = connection.getOutputStream();
        stream.write(json.toString().getBytes(StandardCharsets.UTF_8));
        stream.flush();
        stream.close();
        connection.getInputStream().close();
        connection.disconnect();
    }

    public static class EmbedObject {
        private final List<Field> fields;
        private String title;
        private String description;
        private String url;
        private Color color;
        private Footer footer;
        private Thumbnail thumbnail;
        private Image image;
        private Author author;

        public EmbedObject() {
            this.fields = new ArrayList<Field>();
        }

        public String getTitle() {
            return this.title;
        }

        public EmbedObject setTitle(final String title) {
            this.title = title;
            return this;
        }

        public String getDescription() {
            return this.description;
        }

        public EmbedObject setDescription(final String description) {
            this.description = description;
            return this;
        }

        public String getUrl() {
            return this.url;
        }

        public EmbedObject setUrl(final String url) {
            this.url = url;
            return this;
        }

        public Color getColor() {
            return this.color;
        }

        public EmbedObject setColor(final Color color) {
            this.color = color;
            return this;
        }

        public Footer getFooter() {
            return this.footer;
        }

        public Thumbnail getThumbnail() {
            return this.thumbnail;
        }

        public EmbedObject setThumbnail(final String url) {
            this.thumbnail = new Thumbnail(url);
            return this;
        }

        public Image getImage() {
            return this.image;
        }

        public EmbedObject setImage(final String url) {
            this.image = new Image(url);
            return this;
        }

        public Author getAuthor() {
            return this.author;
        }

        public List<Field> getFields() {
            return this.fields;
        }

        public EmbedObject setFooter(final String text, final String icon) {
            this.footer = new Footer(text, icon);
            return this;
        }

        public EmbedObject setAuthor(final String name, final String url, final String icon) {
            this.author = new Author(name, url, icon);
            return this;
        }

        public EmbedObject addField(final String name, final String value, final boolean inline) {
            this.fields.add(new Field(name, value, inline));
            return this;
        }

        private class Field {
            private String name;
            private String value;
            private boolean inline;

            private Field(final String name, final String value, final boolean inline) {
                this.name = name;
                this.value = value;
                this.inline = inline;
            }

            public Field(EmbedObject embedObject, String x1, String x2, boolean x3) {
            }

            Field(final EmbedObject embedObject, final String x1, final String x2, final boolean x3, final Object x4) {
                this(embedObject, x1, x2, x3);
            }

            private String getName() {
                return this.name;
            }

            private String getValue() {
                return this.value;
            }

            private boolean isInline() {
                return this.inline;
            }
        }

        private class Author {
            private String name;
            private String url;
            private String iconUrl;

            private Author(final String name, final String url, final String iconUrl) {
                this.name = name;
                this.url = url;
                this.iconUrl = iconUrl;
            }

            public Author(EmbedObject embedObject, String x1, String x2, String x3) {
            }

            Author(final EmbedObject embedObject, final String x1, final String x2, final String x3, final Object x4) {
                this(embedObject, x1, x2, x3);
            }

            private String getName() {
                return this.name;
            }

            private String getUrl() {
                return this.url;
            }

            private String getIconUrl() {
                return this.iconUrl;
            }
        }

        private class Image {
            private String url;

            private Image(final String url) {
                this.url = url;
            }

            public Image(EmbedObject embedObject, String x1) {
            }

            Image(final EmbedObject embedObject, final String x1, final Object x2) {
                this(embedObject, x1);
            }

            private String getUrl() {
                return this.url;
            }
        }

        private class Thumbnail {
            private String url;

            private Thumbnail(final String url) {
                this.url = url;
            }

            public Thumbnail(EmbedObject embedObject, String x1) {
            }

            Thumbnail(final EmbedObject embedObject, final String x1, final Object x2) {
                this(embedObject, x1);
            }

            private String getUrl() {
                return this.url;
            }
        }

        private class Footer {
            private String text;
            private String iconUrl;

            private Footer(final String text, final String iconUrl) {
                this.text = text;
                this.iconUrl = iconUrl;
            }

            public Footer(EmbedObject embedObject, String x1, String x2) {
            }

            Footer(final EmbedObject embedObject, final String x1, final String x2, final Object x3) {
                this(embedObject, x1, x2);
            }

            private String getText() {
                return this.text;
            }

            private String getIconUrl() {
                return this.iconUrl;
            }
        }
    }

    private class JSONObject {
        private HashMap<String, Object> map;

        private JSONObject() {
            this.map = new HashMap<String, Object>();
        }

        public JSONObject(DiscordWebhook commandUtils, HashMap<String, Object> map) {
            this.map = map;
        }

        JSONObject(final DiscordWebhook commandUtils, final Object x1, HashMap<String, Object> map) {
            this(commandUtils, map);
            this.map = map;
        }

        void put(final String key, final Object value) {
            if (value != null) {
                this.map.put(key, value);
            }
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            final Set<Map.Entry<String, Object>> entrySet = this.map.entrySet();
            builder.append("{");
            int i = 0;
            for (final Map.Entry<String, Object> entry : entrySet) {
                final Object val = entry.getValue();
                builder.append(this.quote(entry.getKey())).append(":");
                if (val instanceof String) {
                    builder.append(this.quote(String.valueOf(val)));
                } else if (val instanceof Integer) {
                    builder.append(Integer.valueOf(String.valueOf(val)));
                } else if (val instanceof Boolean) {
                    builder.append(val);
                } else if (val instanceof JSONObject) {
                    builder.append(val);
                } else if (val.getClass().isArray()) {
                    builder.append("[");
                    for (int len = Array.getLength(val), j = 0; j < len; ++j) {
                        builder.append(Array.get(val, j).toString()).append((j != len - 1) ? "," : "");
                    }
                    builder.append("]");
                }
                ++i;
                builder.append((i == entrySet.size()) ? "}" : ",");
            }
            return builder.toString();
        }

        private String quote(final String string) {
            return "\"" + string + "\"";
        }
    }
}