package express.database;

class BrowserDocumentation {
  static final String docs =
      "<h1 id=\"nosqlite\">NoSQLite</h1>\n" +
      "<p>A single file NoSQL database utilizing SQLite JSON1 extension.</p>\n" +
      "<p>It&#39;s a server-less embedded document database, ideal for small web applications.</p>\n" +
      "<p><strong>It features:</strong></p>\n" +
      "<ul>\n" +
      "<li>Embedded key-value object store</li>\n" +
      "<li>Single file store</li>\n" +
      "<li>Very fast and lightweight MongoDB like API</li>\n" +
      "<li>Full text search capability</li>\n" +
      "<li>Observable store</li>\n" +
      "</ul>\n" +
      "<pre><code class=\"lang-java\">import static nosqlite.Database.collection;\n" +
      "\n" +
      "<span class=\"hljs-keyword\">User</span> <span class=\"hljs-title\">john</span> = new User(<span class=\"hljs-string\">\"John\"</span>);\n" +
      "collection(<span class=\"hljs-string\">\"User\"</span>).save(john);  // create <span class=\"hljs-keyword\">or</span> update <span class=\"hljs-keyword\">user</span>\n" +
      "\n" +
      "<span class=\"hljs-title\">List</span><span class=\"hljs-tag\">&lt;User&gt;</span> users = collection(<span class=\"hljs-string\">\"User\"</span>).find();  // get all users\n" +
      "</code></pre>\n" +
      "<h2 id=\"table-of-content\">Table of content</h2>\n" +
      "<ul>\n" +
      "<li><a href=\"#getting-started\">Getting started</a></li>\n" +
      "<li><a href=\"#collectionconfig\">CollectionConfig</a><ul>\n" +
      "<li><a href=\"#watcher\">Watcher</a></li>\n" +
      "<li><a href=\"#browser\">Browser</a></li>\n" +
      "</ul>\n" +
      "</li>\n" +
      "<li><a href=\"#document\">Document</a></li>\n" +
      "<li><a href=\"#collection-methods\">Collection methods</a><ul>\n" +
      "<li><a href=\"#filters\">Filters</a></li>\n" +
      "<li><a href=\"#findoptions\">FindOptions</a></li>\n" +
      "</ul>\n" +
      "</li>\n" +
      "<li><a href=\"#collection-examples\">Collection Examples</a></li>\n" +
      "<li><a href=\"#filter-nested-objects\">Filter nested objects</a></li>\n" +
      "<li><a href=\"#import\">Import</a></li>\n" +
      "<li><a href=\"#export\">Export</a></li>\n" +
      "<li><a href=\"#drop\">Drop</a><ul>\n" +
      "<li><a href=\"#important-note\">Important note</a></li>\n" +
      "</ul>\n" +
      "</li>\n" +
      "</ul>\n" +
      "\n" +
      "<h2 id=\"getting-started\">Getting started</h2>\n" +
      "<p>Collections can be used with the static <code>collection()</code>-method to manipulate the database.\n" +
      "<strong>collection()</strong> takes either a String with the classname, case sensitive, or the Class itself.\n" +
      "This will create a database-file in your project. Easy to deploy or share. </p>\n" +
      "<pre><code class=\"lang-java\"><span class=\"hljs-keyword\">import</span> <span class=\"hljs-keyword\">static</span> nosqlite.Database.collection;\n" +
      "\n" +
      "<span class=\"hljs-comment\">// creates a database-file in /db-folder called 'data.db'</span>\n" +
      "User john = <span class=\"hljs-keyword\">new</span> User(<span class=\"hljs-string\">\"John\"</span>);\n" +
      "<span class=\"hljs-comment\">// generates an UUID</span>\n" +
      "collection(<span class=\"hljs-string\">\"User\"</span>).save(john); \n" +
      "\n" +
      "User jane = collection(<span class=\"hljs-string\">\"User\"</span>).findById(<span class=\"hljs-string\">\"lic4XCz2kxSOn4vr0D8BV\"</span>);\n" +
      "\n" +
      "jane.setAge(<span class=\"hljs-number\">30</span>);\n" +
      "<span class=\"hljs-comment\">// updates document with same UUID</span>\n" +
      "collection(<span class=\"hljs-string\">\"User\"</span>).save(jane); \n" +
      "\n" +
      "<span class=\"hljs-comment\">// delete Jane</span>\n" +
      "collection(<span class=\"hljs-string\">\"User\"</span>).deleteById(<span class=\"hljs-string\">\"lic4XCz2kxSOn4vr0D8BV\"</span>); \n" +
      "\n" +
      "List&lt;User&gt; users = collection(<span class=\"hljs-string\">\"User\"</span>).<span class=\"hljs-built_in\">find</span>();\n" +
      "List&lt;User&gt; users = collection(User.<span class=\"hljs-keyword\">class</span>).<span class=\"hljs-built_in\">find</span>();\n" +
      "\n" +
      "List&lt;User&gt; usersNamedJohn = collection(<span class=\"hljs-string\">\"User\"</span>).<span class=\"hljs-built_in\">find</span>(eq(<span class=\"hljs-string\">\"name\"</span>, <span class=\"hljs-string\">\"John\"</span>));\n" +
      "\n" +
      "<span class=\"hljs-comment\">// or with the statement syntax</span>\n" +
      "List&lt;User&gt; usersNamedJohn = collection(<span class=\"hljs-string\">\"User\"</span>).<span class=\"hljs-built_in\">find</span>(<span class=\"hljs-string\">\"name==John\"</span>);\n" +
      "</code></pre>\n" +
      "<p>Watch a collection on changes</p>\n" +
      "<pre><code class=\"lang-java\"><span class=\"hljs-regexp\">//</span> watchData has <span class=\"hljs-number\">3</span> fields. \n" +
      "<span class=\"hljs-regexp\">//</span> model - <span class=\"hljs-keyword\">is</span> the <span class=\"hljs-built_in\">document</span> <span class=\"hljs-class\"><span class=\"hljs-keyword\">class</span> <span class=\"hljs-title\">that</span> <span class=\"hljs-title\">was</span> <span class=\"hljs-title\">triggered</span> </span>\n" +
      "<span class=\"hljs-regexp\">//</span> event - <span class=\"hljs-keyword\">is</span> the event triggered - <span class=\"hljs-string\">'insert'</span>, <span class=\"hljs-string\">'update'</span> <span class=\"hljs-keyword\">or</span> <span class=\"hljs-string\">'delete'</span>\n" +
      "<span class=\"hljs-regexp\">//</span> data - <span class=\"hljs-keyword\">is</span> a list with effected documents\n" +
      "collection(<span class=\"hljs-string\">\"User\"</span>).watch(watchData -&gt; {\n" +
      "    List&lt;User&gt; effectedUsers = (List&lt;User&gt;) watchData.data;\n" +
      "\n" +
      "    <span class=\"hljs-keyword\">switch</span>(watchData.event) {\n" +
      "        case <span class=\"hljs-string\">\"insert\"</span>: <span class=\"hljs-regexp\">//</span> <span class=\"hljs-literal\">on</span> created <span class=\"hljs-built_in\">document</span>\n" +
      "        <span class=\"hljs-keyword\">break</span>;\n" +
      "\n" +
      "        case <span class=\"hljs-string\">\"update\"</span>: <span class=\"hljs-regexp\">//</span> <span class=\"hljs-literal\">on</span> updated <span class=\"hljs-built_in\">document</span>\n" +
      "        <span class=\"hljs-keyword\">break</span>;\n" +
      "\n" +
      "        case <span class=\"hljs-string\">\"delete\"</span>: <span class=\"hljs-regexp\">//</span> <span class=\"hljs-literal\">on</span> deleted <span class=\"hljs-built_in\">document</span>\n" +
      "        <span class=\"hljs-keyword\">break</span>;\n" +
      "    }\n" +
      "});\n" +
      "</code></pre>\n" +
      "<h3 id=\"collectionconfig\">CollectionConfig</h3>\n" +
      "<p>CollectionConfig can be passed when enabling collections to set certain options.\n" +
      "Options available are:</p>\n" +
      "<ul>\n" +
      "<li><em>dbPath</em> - The default path is &quot;db/data.db&quot;. You can override that with this option. </li>\n" +
      "<li><em>runAsync</em> - Enables threaded async calls to the database.</li>\n" +
      "<li><em>useWatcher</em> - Enable WebSocket listener on collection changes. With <em>runAsync</em> this triggers on a different thread.</li>\n" +
      "<li><em>useBrowser</em> - Enable collection browser (good when developing)</li>\n" +
      "</ul>\n" +
      "<p><strong>Note:</strong> options must be called before any other call with collection()! </p>\n" +
      "<p>You can pass one or multiple options when enabling collections:</p>\n" +
      "<pre><code class=\"lang-java\"><span class=\"hljs-comment\">// default options </span>\n" +
      "collection(<span class=\"hljs-keyword\">option</span> -&gt; {\n" +
      "    <span class=\"hljs-keyword\">option</span>.dbPath = <span class=\"hljs-string\">\"db/data.db\"</span>;\n" +
      "    <span class=\"hljs-keyword\">option</span>.runAsync = <span class=\"hljs-literal\">true</span>; \n" +
      "    <span class=\"hljs-keyword\">option</span>.useWatcher = <span class=\"hljs-literal\">false</span>;\n" +
      "    <span class=\"hljs-keyword\">option</span>.useBrowser = <span class=\"hljs-literal\">false</span>; \n" +
      "});\n" +
      "</code></pre>\n" +
      "<h4 id=\"watcher\">Watcher</h4>\n" +
      "<p><strong>useWatcher</strong> starts an <code>WebSocket</code> endpoint in the database that will send <em>watchData</em> when a change happens.</p>\n" +
      "<p><strong>WatchData</strong> is an object containing <em>model</em>, <em>event</em>, <em>data</em>.</p>\n" +
      "<ul>\n" +
      "<li><em>model</em>: The collection that were triggered</li>\n" +
      "<li><em>event</em>: The event that was triggered, either &#39;insert&#39;, &#39;update&#39; or &#39;delete&#39;</li>\n" +
      "<li><em>data</em>: List of documents that are related to the change</li>\n" +
      "</ul>\n" +
      "<p>To listen to these events on the client you have to create a WebSocket connection to <code>&#39;ws://&lt;hostname&gt;:&lt;port&gt;/watch-collections&#39;</code>.</p>\n" +
      "<pre><code class=\"lang-js\"><span class=\"hljs-keyword\">let</span> <span class=\"hljs-keyword\">ws</span> = <span class=\"hljs-keyword\">new</span> WebSocket(<span class=\"hljs-string\">'ws://localhost:4000/watch-collections'</span>)\n" +
      "</code></pre>\n" +
      "<p>With the webSocket you can listen to messages from the collection channel.</p>\n" +
      "<pre><code class=\"lang-js\">ws.onmessage = <span class=\"hljs-function\"><span class=\"hljs-params\">messageEvent</span> =&gt;</span> {\n" +
      "    <span class=\"hljs-keyword\">const</span> watchData = <span class=\"hljs-built_in\">JSON</span>.parse(messageEvent.data);\n" +
      "\n" +
      "    <span class=\"hljs-comment\">// deconstruct model, event and data from watchData</span>\n" +
      "    <span class=\"hljs-keyword\">const</span> { model, event, data } = watchData;\n" +
      "\n" +
      "    <span class=\"hljs-keyword\">if</span>(model == <span class=\"hljs-string\">'BlogPost'</span>) {\n" +
      "        <span class=\"hljs-comment\">// do something with BlogPost</span>\n" +
      "    } \n" +
      "    <span class=\"hljs-keyword\">else</span> <span class=\"hljs-keyword\">if</span>(model == <span class=\"hljs-string\">'Message'</span>) {\n" +
      "        <span class=\"hljs-comment\">// do something with Message</span>\n" +
      "    }\n" +
      "};\n" +
      "</code></pre>\n" +
      "<h4 id=\"example\">Example</h4>\n" +
      "<p>Java:</p>\n" +
      "<pre><code class=\"lang-java\">collection(<span class=\"hljs-built_in\">option</span> -&gt; {\n" +
      "    <span class=\"hljs-built_in\">option</span>.useWatcher = <span class=\"hljs-literal\">true</span>;\n" +
      "});\n" +
      "</code></pre>\n" +
      "<p>JavaScript:</p>\n" +
      "<pre><code class=\"lang-js\">ws.onmessage = <span class=\"hljs-function\"><span class=\"hljs-params\">messageEvent</span> =&gt;</span> {\n" +
      "    <span class=\"hljs-keyword\">const</span> watchData = <span class=\"hljs-built_in\">JSON</span>.parse(messageEvent.data);\n" +
      "\n" +
      "    <span class=\"hljs-comment\">// deconstruct model, event and data from watchData</span>\n" +
      "    <span class=\"hljs-keyword\">const</span> { model, event, data } = watchData;\n" +
      "\n" +
      "    <span class=\"hljs-keyword\">switch</span>(event) {\n" +
      "        <span class=\"hljs-keyword\">case</span> <span class=\"hljs-string\">'insert'</span>:\n" +
      "            <span class=\"hljs-comment\">// add post to list</span>\n" +
      "            model == <span class=\"hljs-string\">'BlogPost'</span> &amp;&amp; posts.push(data[<span class=\"hljs-number\">0</span>]);\n" +
      "            model == <span class=\"hljs-string\">'Message'</span> &amp;&amp; <span class=\"hljs-comment\">// add message to list</span>\n" +
      "        <span class=\"hljs-keyword\">break</span>;\n" +
      "        <span class=\"hljs-keyword\">case</span> <span class=\"hljs-string\">'update'</span>:\n" +
      "        <span class=\"hljs-keyword\">break</span>;\n" +
      "        <span class=\"hljs-keyword\">case</span> <span class=\"hljs-string\">'delete'</span>:\n" +
      "            <span class=\"hljs-comment\">// remove post from list</span>\n" +
      "            model == <span class=\"hljs-string\">'BlogPost'</span> &amp;&amp; (posts = posts.filter(<span class=\"hljs-function\"><span class=\"hljs-params\">post</span> =&gt;</span> post.id !== data[<span class=\"hljs-number\">0</span>].id));\n" +
      "        <span class=\"hljs-keyword\">break</span>;\n" +
      "    };\n" +
      "\n" +
      "    <span class=\"hljs-comment\">// update </span>\n" +
      "    renderPosts();\n" +
      "};\n" +
      "</code></pre>\n" +
      "<h4 id=\"browser\">Browser</h4>\n" +
      "<p><strong>useBrowser</strong> will enable the collection browser. This lets you peek at the stored data while developing. \n" +
      "It might be a good idea to disable this when deploying to save CPU and RAM.</p>\n" +
      "<pre><code class=\"lang-java\">collection(<span class=\"hljs-built_in\">option</span> -&gt; {\n" +
      "    <span class=\"hljs-built_in\">option</span>.useBrowser = <span class=\"hljs-literal\">true</span>; \n" +
      "});\n" +
      "</code></pre>\n" +
      "<h2 id=\"document\">Document</h2>\n" +
      "<p>Collections can be used as a simple key/value store, but it&#39;s true potential is when using it with POJOs. (Plain Old Java Objects)\n" +
      "When using POJOs the following two annotations must be present in the class.</p>\n" +
      "<h3 id=\"-document-annotation\">@Document Annotation</h3>\n" +
      "<p>Marks a class to be used with a collection. Is required if an object is going to be saved to the collection.</p>\n" +
      "<h3 id=\"-id-annotation\">@Id Annotation</h3>\n" +
      "<p>Each object in a Collection must be uniquely identified by a String field marked with <strong>@Id</strong> annotation. The collection maintains an unique index on that field to identify the objects.\n" +
      "If no id is manually set, the Collection will generate an UUID to that field when inserted or saved.</p>\n" +
      "<pre><code class=\"lang-java\"><span class=\"hljs-meta\"><span class=\"hljs-meta-keyword\">import</span> nosqlite.annotations.Document;</span>\n" +
      "<span class=\"hljs-meta\"><span class=\"hljs-meta-keyword\">import</span> nosqlite.annotations.Id;</span>\n" +
      "\n" +
      "@Document\n" +
      "<span class=\"hljs-keyword\">public</span> <span class=\"hljs-class\"><span class=\"hljs-keyword\">class</span> <span class=\"hljs-title\">MyType</span> </span>{\n" +
      "\n" +
      "    @Id\n" +
      "    <span class=\"hljs-keyword\">private</span> String id;\n" +
      "    <span class=\"hljs-keyword\">private</span> String name;\n" +
      "}\n" +
      "</code></pre>\n" +
      "<h2 id=\"collection-methods\">Collection methods</h2>\n" +
      "<p>To use the collection you need to add which document to query for in the collection parameter, ex <code>collection(&quot;User&quot;)</code> will only query for Users.\n" +
      "Data is stored in the collection as JSON, and the <code>find()</code>-methods parse this JSON to target class.\n" +
      "<code>findAsJson()</code>-methods is MUCH MUCH faster, because no parsing is required. This is good when only sending data from a collection directly over the network.</p>\n" +
      "<p><strong>Table 1. Collection methods</strong></p>\n" +
      "<table>\n" +
      "<thead>\n" +
      "<tr>\n" +
      "<th>Operation</th>\n" +
      "<th>Method</th>\n" +
      "<th>Description</th>\n" +
      "</tr>\n" +
      "</thead>\n" +
      "<tbody>\n" +
      "<tr>\n" +
      "<td>Get all documents</td>\n" +
      "<td>find(Filter)</td>\n" +
      "<td>Returns a list with objects. If no filter is used find() will return ALL documents.</td>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td>Get one document</td>\n" +
      "<td>findOne(Filter)</td>\n" +
      "<td>Returns first found document.</td>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td>Get document with id</td>\n" +
      "<td>findById(id)</td>\n" +
      "<td>Returns the object with matching id.</td>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td>Get all documents as JSON</td>\n" +
      "<td>findAsJson(Filter)</td>\n" +
      "<td>Returns a list with objects as JSON. If no filter is used find() will return ALL documents.</td>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td>Get one document as JSON</td>\n" +
      "<td>findOneAsJson(Filter)</td>\n" +
      "<td>Returns first found document as JSON.</td>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td>Get document with id as JSON</td>\n" +
      "<td>findByIdAsJson(id)</td>\n" +
      "<td>Returns the object with matching id as JSON.</td>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td>Create or Update a document</td>\n" +
      "<td>save(Object)</td>\n" +
      "<td>Creates a new document in the collection if no id is present. If theres an id save() will update the existing document in the collection. Can save an array of documents.</td>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td>Update documents</td>\n" +
      "<td>updateField(fieldName, newValue)</td>\n" +
      "<td>Update all documents fields with new value.</td>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td>Update a document field with Object</td>\n" +
      "<td>updateField(Object, fieldName, newValue)</td>\n" +
      "<td>Updates the document field with matching id.</td>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td>Update a document field with id</td>\n" +
      "<td>updateFieldById(id, fieldName, newValue)</td>\n" +
      "<td>Updates the document field with matching id.</td>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td>Update documents</td>\n" +
      "<td>changeFieldName(newFieldName, oldFieldName)</td>\n" +
      "<td>Change field name on all documents.</td>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td>Update documents</td>\n" +
      "<td>removeField(fieldName)</td>\n" +
      "<td>Removes field from all documents.</td>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td>Delete a document</td>\n" +
      "<td>delete(Document)</td>\n" +
      "<td>Deletes the document with matching id.</td>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td>Delete documents</td>\n" +
      "<td>delete(Filter)</td>\n" +
      "<td>Deletes all documents matching the filter.</td>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td>Delete a document with id</td>\n" +
      "<td>deleteById(id)</td>\n" +
      "<td>Deletes the document with matching id.</td>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td>Get number of documents</td>\n" +
      "<td>count()</td>\n" +
      "<td>Returns the count of all documents in a collection.</td>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td>Watch a collection</td>\n" +
      "<td>watch(lambda)</td>\n" +
      "<td>Register a watcher that triggers on changes in the collection.</td>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td>Watch a collection on an event</td>\n" +
      "<td>watch(event, lambda)</td>\n" +
      "<td>Register a watcher that triggers on changes at target event in the collection.</td>\n" +
      "</tr>\n" +
      "</tbody>\n" +
      "</table>\n" +
      "<p><strong>Table 1.2. Collection as a key/value store methods</strong></p>\n" +
      "<p>When using the collection as a key/value store you can name the collection anything you want.</p>\n" +
      "<pre><code class=\"lang-java\">collection(<span class=\"hljs-string\">\"pets\"</span>).<span class=\"hljs-built_in\">put</span>(<span class=\"hljs-string\">\"snuggles\"</span>, <span class=\"hljs-keyword\">new</span> Cat(<span class=\"hljs-string\">\"Snuggles\"</span>));\n" +
      "collection(<span class=\"hljs-string\">\"pets\"</span>).<span class=\"hljs-built_in\">put</span>(<span class=\"hljs-string\">\"pluto\"</span>, <span class=\"hljs-keyword\">new</span> Dog(<span class=\"hljs-string\">\"Pluto\"</span>));\n" +
      "\n" +
      "Dog pluto = collection(<span class=\"hljs-string\">\"pets\"</span>).<span class=\"hljs-built_in\">get</span>(<span class=\"hljs-string\">\"pluto\"</span>, Dog.<span class=\"hljs-keyword\">class</span>);\n" +
      "</code></pre>\n" +
      "<table>\n" +
      "<thead>\n" +
      "<tr>\n" +
      "<th>Operation</th>\n" +
      "<th>Method</th>\n" +
      "<th>Description</th>\n" +
      "</tr>\n" +
      "</thead>\n" +
      "<tbody>\n" +
      "<tr>\n" +
      "<td>Get value by key</td>\n" +
      "<td>get(key)</td>\n" +
      "<td>Returns an object as JSON.</td>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td>Get value by key as a POJO</td>\n" +
      "<td>get(key, class)</td>\n" +
      "<td>Returns an object parsed to target class.</td>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td>Store object at key</td>\n" +
      "<td>put(key, value)</td>\n" +
      "<td>Stores the value as JSON at target key. Replaces value if key exists.</td>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td>Store object at key</td>\n" +
      "<td>putIfAbsent(key, value)</td>\n" +
      "<td>Stores the value as JSON at target key. Does not replace value if key exists.</td>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td>Remove value by key</td>\n" +
      "<td>remove(key)</td>\n" +
      "<td>Removes both key and value.</td>\n" +
      "</tr>\n" +
      "</tbody>\n" +
      "</table>\n" +
      "<h3 id=\"filters\">Filters</h3>\n" +
      "<p>Filter are the selectors in the collection’s find operation. It matches documents in the collection depending on the criteria provided and returns a list of objects.</p>\n" +
      "<p>Make sure you import the static method <strong>Filter</strong>.</p>\n" +
      "<pre><code class=\"lang-java\"><span class=\"hljs-meta\"><span class=\"hljs-meta-keyword\">import</span> static nosqlite.utilities.Filter.*;</span>\n" +
      "</code></pre>\n" +
      "<p><strong>Table 2. Comparison Filter</strong></p>\n" +
      "<table>\n" +
      "<thead>\n" +
      "<tr>\n" +
      "<th>Filter</th>\n" +
      "<th>Method</th>\n" +
      "<th>Description</th>\n" +
      "</tr>\n" +
      "</thead>\n" +
      "<tbody>\n" +
      "<tr>\n" +
      "<td>Equals</td>\n" +
      "<td>eq(String, Object)</td>\n" +
      "<td>Matches values that are equal to a specified value.</td>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td>NotEquals</td>\n" +
      "<td>ne(String, Object)</td>\n" +
      "<td>Matches values that are not equal to a specified value.</td>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td>Greater</td>\n" +
      "<td>gt(String, Object)</td>\n" +
      "<td>Matches values that are greater than a specified value.</td>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td>GreaterEquals</td>\n" +
      "<td>gte(String, Object)</td>\n" +
      "<td>Matches values that are greater than or equal to a specified value.</td>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td>Lesser</td>\n" +
      "<td>lt(String, Object)</td>\n" +
      "<td>Matches values that are less than a specified value.</td>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td>LesserEquals</td>\n" +
      "<td>lte(String, Object)</td>\n" +
      "<td>Matches values that are less than or equal to a specified value.</td>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td>In</td>\n" +
      "<td>in(String, Object[])</td>\n" +
      "<td>Matches any of the values specified in an array.</td>\n" +
      "</tr>\n" +
      "</tbody>\n" +
      "</table>\n" +
      "<p><strong>Table 3. Logical Filters</strong></p>\n" +
      "<table>\n" +
      "<thead>\n" +
      "<tr>\n" +
      "<th>Filter</th>\n" +
      "<th>Method</th>\n" +
      "<th>Description</th>\n" +
      "</tr>\n" +
      "</thead>\n" +
      "<tbody>\n" +
      "<tr>\n" +
      "<td>Not</td>\n" +
      "<td>not(Filter)</td>\n" +
      "<td>Inverts the effect of a filter and returns results that do not match the filter.</td>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td>Or</td>\n" +
      "<td>or(Filter...)</td>\n" +
      "<td>Joins filters with a logical OR returns all ids of the documents that match the conditions of either filter.</td>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td>And</td>\n" +
      "<td>and(Filter...)</td>\n" +
      "<td>Joins filters with a logical AND returns all ids of the documents that match the conditions of both filters.</td>\n" +
      "</tr>\n" +
      "</tbody>\n" +
      "</table>\n" +
      "<p><strong>Table 4. Text Filters</strong></p>\n" +
      "<table>\n" +
      "<thead>\n" +
      "<tr>\n" +
      "<th>Filter</th>\n" +
      "<th>Method</th>\n" +
      "<th>Description</th>\n" +
      "</tr>\n" +
      "</thead>\n" +
      "<tbody>\n" +
      "<tr>\n" +
      "<td>Text</td>\n" +
      "<td>text(String, String)</td>\n" +
      "<td>Performs full-text search. Same rules as <a target=\"_blank\" href=\"https://www.w3schools.com/sql/sql_like.asp\">SQL LIKE</a></td>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td>Regex</td>\n" +
      "<td>regex(String, String)</td>\n" +
      "<td>Selects documents where values match a specified regular expression.</td>\n" +
      "</tr>\n" +
      "</tbody>\n" +
      "</table>\n" +
      "<h3 id=\"findoptions\">FindOptions</h3>\n" +
      "<p>A FindOptions is used to specify search options. It provides pagination as well as sorting mechanism.\n" +
      "The config syntax with lambda is more clear and easier to read.</p>\n" +
      "<p>Example</p>\n" +
      "<pre><code class=\"lang-java\">// sorts all documents by age <span class=\"hljs-keyword\">in</span> ascending order <span class=\"hljs-keyword\">then</span> take <span class=\"hljs-built_in\">first</span> <span class=\"hljs-number\">10</span> documents <span class=\"hljs-keyword\">and</span> <span class=\"hljs-built_in\">return</span> as a List\n" +
      "List&lt;User&gt; users = collection(<span class=\"hljs-string\">\"User\"</span>).find(null, <span class=\"hljs-string\">\"age=asc\"</span>, <span class=\"hljs-number\">10</span>, <span class=\"hljs-number\">0</span>);\n" +
      "\n" +
      "// <span class=\"hljs-keyword\">or</span> with FindOptions\n" +
      "List&lt;User&gt; users = collection(<span class=\"hljs-string\">\"User\"</span>).find(<span class=\"hljs-built_in\">op</span> -&gt; {\n" +
      "        <span class=\"hljs-built_in\">op</span>.<span class=\"hljs-built_in\">sort</span> = <span class=\"hljs-string\">\"age=asc\"</span>;\n" +
      "        <span class=\"hljs-built_in\">op</span>.<span class=\"hljs-built_in\">limit</span> = <span class=\"hljs-number\">10</span>;\n" +
      "    });\n" +
      "</code></pre>\n" +
      "<pre><code class=\"lang-java\">// sorts the documents by age <span class=\"hljs-keyword\">in</span> ascending <span class=\"hljs-keyword\">order</span>\n" +
      "<span class=\"hljs-title\">List</span><span class=\"hljs-tag\">&lt;User&gt;</span> users = collection(<span class=\"hljs-string\">\"User\"</span>).find(null, <span class=\"hljs-string\">\"age=asc\"</span>, <span class=\"hljs-number\">0</span>, <span class=\"hljs-number\">0</span>);\n" +
      "\n" +
      "// <span class=\"hljs-keyword\">or</span> with FindOptions\n" +
      "List<span class=\"hljs-tag\">&lt;User&gt;</span> users = collection(<span class=\"hljs-string\">\"User\"</span>).find(<span class=\"hljs-keyword\">op</span> -&gt; {\n" +
      "        <span class=\"hljs-keyword\">op</span>.filter = <span class=\"hljs-string\">\"age=asc\"</span>;\n" +
      "    });\n" +
      "</code></pre>\n" +
      "<pre><code class=\"lang-java\">// fetch <span class=\"hljs-number\">10</span> documents starting from <span class=\"hljs-attr\">offset</span> = <span class=\"hljs-number\">2</span>\n" +
      "List&lt;User&gt; <span class=\"hljs-attr\">users</span> = collection(<span class=\"hljs-string\">\"User\"</span>).find(<span class=\"hljs-number\">10</span>, <span class=\"hljs-number\">2</span>);\n" +
      "\n" +
      "// <span class=\"hljs-literal\">or</span> <span class=\"hljs-keyword\">with</span> FindOptions\n" +
      "List&lt;User&gt; <span class=\"hljs-attr\">users</span> = collection(<span class=\"hljs-string\">\"User\"</span>).find(op -&gt; {\n" +
      "        op.<span class=\"hljs-attr\">limit</span> = <span class=\"hljs-number\">10</span>;\n" +
      "        op.<span class=\"hljs-attr\">offset</span> = <span class=\"hljs-number\">2</span>;\n" +
      "    });\n" +
      "</code></pre>\n" +
      "<h2 id=\"collection-examples\">Collection Examples</h2>\n" +
      "<p><strong>and()</strong></p>\n" +
      "<pre><code class=\"lang-java\">// matches all documents where <span class=\"hljs-string\">'age'</span> field has value as <span class=\"hljs-number\">30</span> <span class=\"hljs-keyword\">and</span>\n" +
      "// <span class=\"hljs-string\">'name'</span> field has value as John Doe\n" +
      "collection(<span class=\"hljs-string\">\"User\"</span>).find(<span class=\"hljs-keyword\">and</span>(e<span class=\"hljs-string\">q(\"age\", 30)</span>, e<span class=\"hljs-string\">q(\"name\", \"John Doe\")</span>));\n" +
      "<span class=\"hljs-regexp\">//</span> with the statement syntax\n" +
      "collection(<span class=\"hljs-string\">\"User\"</span>).find(<span class=\"hljs-string\">\"age==30 &amp;&amp; name==John Doe\"</span>);\n" +
      "</code></pre>\n" +
      "<p><strong>or()</strong></p>\n" +
      "<pre><code class=\"lang-java\">// matches all documents where <span class=\"hljs-string\">'age'</span> field has value as <span class=\"hljs-number\">30</span> <span class=\"hljs-keyword\">or</span>\n" +
      "// <span class=\"hljs-string\">'name'</span> field has value as John Doe\n" +
      "collection(<span class=\"hljs-string\">\"User\"</span>).find(<span class=\"hljs-keyword\">or</span>(e<span class=\"hljs-string\">q(\"age\", 30)</span>, e<span class=\"hljs-string\">q(\"name\", \"John Doe\")</span>));\n" +
      "<span class=\"hljs-regexp\">//</span> with the statement syntax\n" +
      "collection(<span class=\"hljs-string\">\"User\"</span>).find(<span class=\"hljs-string\">\"age==30 || name==John Doe\"</span>);\n" +
      "</code></pre>\n" +
      "<p><strong>not()</strong></p>\n" +
      "<pre><code class=\"lang-java\">// matches all documents where <span class=\"hljs-string\">'age'</span> field has value <span class=\"hljs-keyword\">not</span> equals to <span class=\"hljs-number\">30</span>\n" +
      "// <span class=\"hljs-keyword\">and</span> name is <span class=\"hljs-keyword\">not</span> John Doe\n" +
      "collection(<span class=\"hljs-string\">\"User\"</span>).find(<span class=\"hljs-keyword\">not</span>(<span class=\"hljs-keyword\">and</span>((e<span class=\"hljs-string\">q(\"age\", 30)</span>, e<span class=\"hljs-string\">q(\"name\", \"John Doe\")</span>)));\n" +
      "<span class=\"hljs-regexp\">//</span> with the statement syntax\n" +
      "collection(<span class=\"hljs-string\">\"User\"</span>).find(<span class=\"hljs-string\">\"!(age==30 &amp;&amp; name==John Doe)\"</span>);\n" +
      "</code></pre>\n" +
      "<p><strong>eq()</strong></p>\n" +
      "<pre><code class=\"lang-java\"><span class=\"hljs-comment\">// matches all documents where 'age' field has value as 30</span>\n" +
      "<span class=\"hljs-selector-tag\">collection</span>(<span class=\"hljs-string\">\"User\"</span>)<span class=\"hljs-selector-class\">.find</span>(eq(<span class=\"hljs-string\">\"age\"</span>, <span class=\"hljs-number\">30</span>));\n" +
      "<span class=\"hljs-comment\">// with the statement syntax</span>\n" +
      "<span class=\"hljs-selector-tag\">collection</span>(<span class=\"hljs-string\">\"User\"</span>)<span class=\"hljs-selector-class\">.find</span>(<span class=\"hljs-string\">\"age==30\"</span>);\n" +
      "</code></pre>\n" +
      "<p><strong>ne()</strong></p>\n" +
      "<pre><code class=\"lang-java\"><span class=\"hljs-comment\">// matches all documents where 'age' field has value not equals to 30</span>\n" +
      "<span class=\"hljs-selector-tag\">collection</span>(<span class=\"hljs-string\">\"User\"</span>)<span class=\"hljs-selector-class\">.find</span>(ne(<span class=\"hljs-string\">\"age\"</span>, <span class=\"hljs-number\">30</span>));\n" +
      "<span class=\"hljs-comment\">// with the statement syntax</span>\n" +
      "<span class=\"hljs-selector-tag\">collection</span>(<span class=\"hljs-string\">\"User\"</span>)<span class=\"hljs-selector-class\">.find</span>(<span class=\"hljs-string\">\"age!=30\"</span>);\n" +
      "</code></pre>\n" +
      "<p><strong>gt()</strong></p>\n" +
      "<pre><code class=\"lang-java\"><span class=\"hljs-comment\">// matches all documents where 'age' field has value greater than 30</span>\n" +
      "<span class=\"hljs-selector-tag\">collection</span>(<span class=\"hljs-string\">\"User\"</span>)<span class=\"hljs-selector-class\">.find</span>(gt(<span class=\"hljs-string\">\"age\"</span>, <span class=\"hljs-number\">30</span>));\n" +
      "<span class=\"hljs-comment\">// with the statement syntax</span>\n" +
      "<span class=\"hljs-selector-tag\">collection</span>(<span class=\"hljs-string\">\"User\"</span>)<span class=\"hljs-selector-class\">.find</span>(<span class=\"hljs-string\">\"age&gt;30\"</span>);\n" +
      "</code></pre>\n" +
      "<p><strong>gte()</strong></p>\n" +
      "<pre><code class=\"lang-java\"><span class=\"hljs-comment\">// matches all documents where 'age' field has value greater than or equal to 30</span>\n" +
      "<span class=\"hljs-selector-tag\">collection</span>(<span class=\"hljs-string\">\"User\"</span>)<span class=\"hljs-selector-class\">.find</span>(gte(<span class=\"hljs-string\">\"age\"</span>, <span class=\"hljs-number\">30</span>));\n" +
      "<span class=\"hljs-comment\">// with the statement syntax</span>\n" +
      "<span class=\"hljs-selector-tag\">collection</span>(<span class=\"hljs-string\">\"User\"</span>)<span class=\"hljs-selector-class\">.find</span>(<span class=\"hljs-string\">\"age&gt;=30\"</span>);\n" +
      "</code></pre>\n" +
      "<p><strong>lt()</strong></p>\n" +
      "<pre><code class=\"lang-java\"><span class=\"hljs-comment\">// matches all documents where 'age' field has value less than 30</span>\n" +
      "<span class=\"hljs-selector-tag\">collection</span>(<span class=\"hljs-string\">\"User\"</span>)<span class=\"hljs-selector-class\">.find</span>(lt(<span class=\"hljs-string\">\"age\"</span>, <span class=\"hljs-number\">30</span>));\n" +
      "<span class=\"hljs-comment\">// with the statement syntax</span>\n" +
      "<span class=\"hljs-selector-tag\">collection</span>(<span class=\"hljs-string\">\"User\"</span>)<span class=\"hljs-selector-class\">.find</span>(<span class=\"hljs-string\">\"age&lt;30\"</span>);\n" +
      "</code></pre>\n" +
      "<p><strong>lte()</strong></p>\n" +
      "<pre><code class=\"lang-java\"><span class=\"hljs-comment\">// matches all documents where 'age' field has value lesser than or equal to 30</span>\n" +
      "<span class=\"hljs-selector-tag\">collection</span>(<span class=\"hljs-string\">\"User\"</span>)<span class=\"hljs-selector-class\">.find</span>(lte(<span class=\"hljs-string\">\"age\"</span>, <span class=\"hljs-number\">30</span>));\n" +
      "<span class=\"hljs-comment\">// with the statement syntax</span>\n" +
      "<span class=\"hljs-selector-tag\">collection</span>(<span class=\"hljs-string\">\"User\"</span>)<span class=\"hljs-selector-class\">.find</span>(<span class=\"hljs-string\">\"age&lt;=30\"</span>);\n" +
      "</code></pre>\n" +
      "<p><strong>in()</strong></p>\n" +
      "<pre><code class=\"lang-java\"><span class=\"hljs-comment\">// matches all documents where 'age' field has value in [20, 30, 40]</span>\n" +
      "collection(<span class=\"hljs-string\">\"User\"</span>).<span class=\"hljs-built_in\">find</span>(<span class=\"hljs-built_in\">in</span>(<span class=\"hljs-string\">\"age\"</span>, <span class=\"hljs-number\">20</span>, <span class=\"hljs-number\">30</span>, <span class=\"hljs-number\">40</span>));\n" +
      "\n" +
      "<span class=\"hljs-built_in\">List</span> ages = <span class=\"hljs-built_in\">List</span>.of(<span class=\"hljs-number\">20</span>, <span class=\"hljs-number\">30</span>, <span class=\"hljs-number\">40</span>);\n" +
      "collection(<span class=\"hljs-string\">\"User\"</span>).<span class=\"hljs-built_in\">find</span>(<span class=\"hljs-built_in\">in</span>(<span class=\"hljs-string\">\"age\"</span>, ages));\n" +
      "\n" +
      "<span class=\"hljs-comment\">// with the statement syntax</span>\n" +
      "collection(<span class=\"hljs-string\">\"User\"</span>).<span class=\"hljs-built_in\">find</span>(<span class=\"hljs-string\">\"age==[20, 30, 40]\"</span>);\n" +
      "</code></pre>\n" +
      "<p><strong>text()</strong>\n" +
      "Same rules as <a target=\"_blank\" href=\"https://www.w3schools.com/sql/sql_like.asp\">SQL LIKE</a></p>\n" +
      "<ul>\n" +
      "<li>The percent sign (%) represents zero, one, or multiple characters</li>\n" +
      "<li>The underscore sign (_) represents one, single character</li>\n" +
      "</ul>\n" +
      "<pre><code class=\"lang-java\">// matches <span class=\"hljs-keyword\">all</span> documents where <span class=\"hljs-symbol\">'address</span>' field start <span class=\"hljs-keyword\">with</span> <span class=\"hljs-string\">\"a\"</span>\n" +
      "collection(<span class=\"hljs-string\">\"User\"</span>).find(<span class=\"hljs-literal\">text</span>(<span class=\"hljs-string\">\"address\"</span>, <span class=\"hljs-string\">\"a%\"</span>));\n" +
      "\n" +
      "// <span class=\"hljs-keyword\">with</span> the statement syntax, applies <span class=\"hljs-keyword\">to</span> <span class=\"hljs-keyword\">all</span> <span class=\"hljs-literal\">text</span>() examples\n" +
      "collection(<span class=\"hljs-string\">\"User\"</span>).find(<span class=\"hljs-string\">\"address=~a%\"</span>);\n" +
      "\n" +
      "// matches <span class=\"hljs-keyword\">all</span> documents where <span class=\"hljs-symbol\">'address</span>' field <span class=\"hljs-keyword\">end</span> <span class=\"hljs-keyword\">with</span> <span class=\"hljs-string\">\"a\"</span>\n" +
      "collection(<span class=\"hljs-string\">\"User\"</span>).find(<span class=\"hljs-literal\">text</span>(<span class=\"hljs-string\">\"address\"</span>, <span class=\"hljs-string\">\"%a\"</span>));\n" +
      "\n" +
      "// matches <span class=\"hljs-keyword\">all</span> documents where <span class=\"hljs-symbol\">'address</span>' field have <span class=\"hljs-string\">\"or\"</span> <span class=\"hljs-keyword\">in</span> any position\n" +
      "collection(<span class=\"hljs-string\">\"User\"</span>).find(<span class=\"hljs-literal\">text</span>(<span class=\"hljs-string\">\"address\"</span>, <span class=\"hljs-string\">\"%or%\"</span>));\n" +
      "\n" +
      "// matches <span class=\"hljs-keyword\">all</span> documents where <span class=\"hljs-symbol\">'address</span>' field have <span class=\"hljs-string\">\"r\"</span> <span class=\"hljs-keyword\">in</span> the second position\n" +
      "collection(<span class=\"hljs-string\">\"User\"</span>).find(<span class=\"hljs-literal\">text</span>(<span class=\"hljs-string\">\"address\"</span>, <span class=\"hljs-string\">\"_r%\"</span>));\n" +
      "\n" +
      "// matches <span class=\"hljs-keyword\">all</span> documents where <span class=\"hljs-symbol\">'address</span>' field start <span class=\"hljs-keyword\">with</span> <span class=\"hljs-string\">\"a\"</span> <span class=\"hljs-keyword\">and</span> are at least <span class=\"hljs-number\">2</span> characters <span class=\"hljs-keyword\">in</span> length\n" +
      "collection(<span class=\"hljs-string\">\"User\"</span>).find(<span class=\"hljs-literal\">text</span>(<span class=\"hljs-string\">\"address\"</span>, <span class=\"hljs-string\">\"a_%\"</span>));\n" +
      "\n" +
      "// matches <span class=\"hljs-keyword\">all</span> documents where <span class=\"hljs-symbol\">'address</span>' field start <span class=\"hljs-keyword\">with</span> <span class=\"hljs-string\">\"a\"</span> <span class=\"hljs-keyword\">and</span> are at least <span class=\"hljs-number\">3</span> characters <span class=\"hljs-keyword\">in</span> length\n" +
      "collection(<span class=\"hljs-string\">\"User\"</span>).find(<span class=\"hljs-literal\">text</span>(<span class=\"hljs-string\">\"address\"</span>, <span class=\"hljs-string\">\"'a__%\"</span>));\n" +
      "\n" +
      "// matches <span class=\"hljs-keyword\">all</span> documents where <span class=\"hljs-symbol\">'address</span>' field start <span class=\"hljs-keyword\">with</span> <span class=\"hljs-string\">\"a\"</span> <span class=\"hljs-keyword\">and</span> ends <span class=\"hljs-keyword\">with</span> <span class=\"hljs-string\">\"o\"</span>\n" +
      "collection(<span class=\"hljs-string\">\"User\"</span>).find(<span class=\"hljs-literal\">text</span>(<span class=\"hljs-string\">\"address\"</span>, <span class=\"hljs-string\">\"a%o\"</span>));\n" +
      "</code></pre>\n" +
      "<p><strong>regex()</strong></p>\n" +
      "<pre><code class=\"lang-java\">// matches <span class=\"hljs-keyword\">all</span> documents where <span class=\"hljs-symbol\">'name</span>' value starts <span class=\"hljs-keyword\">with</span> <span class=\"hljs-symbol\">'jim</span>' <span class=\"hljs-keyword\">or</span> <span class=\"hljs-symbol\">'joe</span>'.\n" +
      "collection(<span class=\"hljs-string\">\"User\"</span>).find(regex(<span class=\"hljs-string\">\"name\"</span>, <span class=\"hljs-string\">\"^(jim|joe).*\"</span>));\n" +
      "// <span class=\"hljs-keyword\">with</span> the statement syntax\n" +
      "collection(<span class=\"hljs-string\">\"User\"</span>).find(<span class=\"hljs-string\">\"name~~^(jim|joe).*\"</span>);\n" +
      "</code></pre>\n" +
      "<h2 id=\"filter-nested-objects\">Filter nested objects</h2>\n" +
      "<p>It&#39;s just as easy to filter nested objects in a collection. Each nested property is accessible with a dot-filter for each level.</p>\n" +
      "<pre><code class=\"lang-java\"><span class=\"hljs-comment\">// matches all documents where a User's cat has an age of 7</span>\n" +
      "<span class=\"hljs-selector-tag\">collection</span>(<span class=\"hljs-string\">\"User\"</span>)<span class=\"hljs-selector-class\">.find</span>(eq(<span class=\"hljs-string\">\"cat.age\"</span>, <span class=\"hljs-number\">7</span>));\n" +
      "<span class=\"hljs-comment\">// with the statement syntax</span>\n" +
      "<span class=\"hljs-selector-tag\">collection</span>(<span class=\"hljs-string\">\"User\"</span>)<span class=\"hljs-selector-class\">.find</span>(<span class=\"hljs-string\">\"cat.age==7\"</span>);\n" +
      "\n" +
      "<span class=\"hljs-comment\">// matches all documents where a User's headphone has a brand of Bose</span>\n" +
      "<span class=\"hljs-selector-tag\">collection</span>(<span class=\"hljs-string\">\"User\"</span>)<span class=\"hljs-selector-class\">.find</span>(eq(<span class=\"hljs-string\">\"accessory.headphones.brand\"</span>, <span class=\"hljs-string\">\"Bose\"</span>));\n" +
      "<span class=\"hljs-comment\">// with the statement syntax</span>\n" +
      "<span class=\"hljs-selector-tag\">collection</span>(<span class=\"hljs-string\">\"User\"</span>)<span class=\"hljs-selector-class\">.find</span>(<span class=\"hljs-string\">\"accessory.headphones.brand==Bose\"</span>);\n" +
      "</code></pre>\n" +
      "<h2 id=\"import\">Import</h2>\n" +
      "<p>The collection supports mocking data as JSON, from example <a  target=\"_blank\" href=\"https://www.mockaroo.com/\">mockaroo.com</a>.\n" +
      "<strong>Note</strong>: Format must be a JSON-array.</p>\n" +
      "<p>Simply select a .json-file and click <code>import</code>. This will append the data to the collection.\n" +
      "It&#39;s important that the fields match the <strong>document</strong> in the collection.</p>\n" +
      "<h2 id=\"export\">Export</h2>\n" +
      "<p>Export will download the current collection as a .json-file.</p>\n" +
      "<p>This file can easily be used to import into the collection, and can serve as a backup.</p>\n" +
      "<p>The .json-file is also created in the db-directory with the name of the document.</p>\n" +
      "<h2 id=\"drop\">Drop</h2>\n" +
      "<p>Will delete all data in the collection.</p>\n" +
      "<h3 id=\"important-note-\">Important note!</h3>\n" +
      "<p>Changing the name of a field will not corrupt the database, but will temporarily remove the value from all documents.\n" +
      "Simply revert the name and the value gets restored. \n" +
      "You can manage fields with <a href=\"#collection-methods\">collection methods</a>.</p>";
}
