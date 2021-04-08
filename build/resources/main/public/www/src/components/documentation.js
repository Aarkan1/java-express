const { onMounted, onUnmounted } = Vue
const { useStore } = Vuex

export default {
    name: 'Documentation',
    setup() {
        const store = useStore()

        onMounted(() => {
            store.commit('setShowDocumentAnchors', true)
            hljs.initHighlightingOnLoad()
        })

        onUnmounted(() => store.commit('setShowDocumentAnchors', false))
    },
    template: `
        <div>
        <h1 id="nosqlite">NoSQLite</h1>
<p>A single file NoSQL database utilizing SQLite JSON1 extension.</p>
<p>It&#39;s a server-less embedded document database, ideal for small web applications.</p>
<p><strong>It features:</strong></p>
<ul>
<li>Embedded key-value object store</li>
<li>Single file store</li>
<li>Very fast and lightweight MongoDB like API</li>
<li>Full text search capability</li>
<li>Observable store</li>
</ul>
<pre><code class="lang-java">import static nosqlite.Database.collection;

<span class="hljs-keyword">User</span> <span class="hljs-title">john</span> = new User(<span class="hljs-string">"John"</span>);
collection(<span class="hljs-string">"User"</span>).save(john);  // create <span class="hljs-keyword">or</span> update <span class="hljs-keyword">user</span>

<span class="hljs-title">List</span><span class="hljs-tag">&lt;User&gt;</span> users = collection(<span class="hljs-string">"User"</span>).find();  // get all users
</code></pre>
<h2 id="table-of-content">Table of content</h2>
<ul>
<li><a href="#getting-started">Getting started</a></li>
<li><a href="#collectionconfig">CollectionConfig</a><ul>
<li><a href="#watcher">Watcher</a></li>
<li><a href="#browser">Browser</a></li>
</ul>
</li>
<li><a href="#document">Document</a></li>
<li><a href="#collection-methods">Collection methods</a><ul>
<li><a href="#filters">Filters</a></li>
<li><a href="#findoptions">FindOptions</a></li>
</ul>
</li>
<li><a href="#collection-examples">Collection Examples</a></li>
<li><a href="#filter-nested-objects">Filter nested objects</a></li>
<li><a href="#import">Import</a></li>
<li><a href="#export">Export</a></li>
<li><a href="#drop">Drop</a><ul>
<li><a href="#important-note">Important note</a></li>
</ul>
</li>
</ul>

<h2 id="getting-started">Getting started</h2>
<p>Collections can be used with the static <code>collection()</code>-method to manipulate the database.
<strong>collection()</strong> takes either a String with the classname, case sensitive, or the Class itself.
This will create a database-file in your project. Easy to deploy or share. </p>
<pre><code class="lang-java"><span class="hljs-keyword">import</span> <span class="hljs-keyword">static</span> nosqlite.Database.collection;

<span class="hljs-comment">// creates a database-file in /db-folder called 'data.db'</span>
User john = <span class="hljs-keyword">new</span> User(<span class="hljs-string">"John"</span>);
<span class="hljs-comment">// generates an UUID</span>
collection(<span class="hljs-string">"User"</span>).save(john); 

User jane = collection(<span class="hljs-string">"User"</span>).findById(<span class="hljs-string">"lic4XCz2kxSOn4vr0D8BV"</span>);

jane.setAge(<span class="hljs-number">30</span>);
<span class="hljs-comment">// updates document with same UUID</span>
collection(<span class="hljs-string">"User"</span>).save(jane); 

<span class="hljs-comment">// delete Jane</span>
collection(<span class="hljs-string">"User"</span>).deleteById(<span class="hljs-string">"lic4XCz2kxSOn4vr0D8BV"</span>); 

List&lt;User&gt; users = collection(<span class="hljs-string">"User"</span>).<span class="hljs-built_in">find</span>();
List&lt;User&gt; users = collection(User.<span class="hljs-keyword">class</span>).<span class="hljs-built_in">find</span>();

List&lt;User&gt; usersNamedJohn = collection(<span class="hljs-string">"User"</span>).<span class="hljs-built_in">find</span>(eq(<span class="hljs-string">"name"</span>, <span class="hljs-string">"John"</span>));

<span class="hljs-comment">// or with the statement syntax</span>
List&lt;User&gt; usersNamedJohn = collection(<span class="hljs-string">"User"</span>).<span class="hljs-built_in">find</span>(<span class="hljs-string">"name==John"</span>);
</code></pre>
<p>Watch a collection on changes</p>
<pre><code class="lang-java"><span class="hljs-regexp">//</span> watchData has <span class="hljs-number">3</span> fields. 
<span class="hljs-regexp">//</span> model - <span class="hljs-keyword">is</span> the <span class="hljs-built_in">document</span> <span class="hljs-class"><span class="hljs-keyword">class</span> <span class="hljs-title">that</span> <span class="hljs-title">was</span> <span class="hljs-title">triggered</span> </span>
<span class="hljs-regexp">//</span> event - <span class="hljs-keyword">is</span> the event triggered - <span class="hljs-string">'insert'</span>, <span class="hljs-string">'update'</span> <span class="hljs-keyword">or</span> <span class="hljs-string">'delete'</span>
<span class="hljs-regexp">//</span> data - <span class="hljs-keyword">is</span> a list with effected documents
collection(<span class="hljs-string">"User"</span>).watch(watchData -&gt; {
    List&lt;User&gt; effectedUsers = (List&lt;User&gt;) watchData.data;

    <span class="hljs-keyword">switch</span>(watchData.event) {
        case <span class="hljs-string">"insert"</span>: <span class="hljs-regexp">//</span> <span class="hljs-literal">on</span> created <span class="hljs-built_in">document</span>
        <span class="hljs-keyword">break</span>;

        case <span class="hljs-string">"update"</span>: <span class="hljs-regexp">//</span> <span class="hljs-literal">on</span> updated <span class="hljs-built_in">document</span>
        <span class="hljs-keyword">break</span>;

        case <span class="hljs-string">"delete"</span>: <span class="hljs-regexp">//</span> <span class="hljs-literal">on</span> deleted <span class="hljs-built_in">document</span>
        <span class="hljs-keyword">break</span>;
    }
});
</code></pre>
<h3 id="collectionconfig">CollectionConfig</h3>
<p>CollectionConfig can be passed when enabling collections to set certain options.
Options available are:</p>
<ul>
<li><em>dbPath</em> - The default path is &quot;db/data.db&quot;. You can override that with this option. </li>
<li><em>runAsync</em> - Enables threaded async calls to the database.</li>
<li><em>useWatcher</em> - Enable WebSocket listener on collection changes. With <em>runAsync</em> this triggers on a different thread.</li>
<li><em>useBrowser</em> - Enable collection browser (good when developing)</li>
</ul>
<p><strong>Note:</strong> options must be called before any other call with collection()! </p>
<p>You can pass one or multiple options when enabling collections:</p>
<pre><code class="lang-java"><span class="hljs-comment">// default options </span>
collection(<span class="hljs-keyword">option</span> -&gt; {
    <span class="hljs-keyword">option</span>.dbPath = <span class="hljs-string">"db/data.db"</span>;
    <span class="hljs-keyword">option</span>.runAsync = <span class="hljs-literal">true</span>; 
    <span class="hljs-keyword">option</span>.useWatcher = <span class="hljs-literal">false</span>;
    <span class="hljs-keyword">option</span>.useBrowser = <span class="hljs-literal">false</span>; 
});
</code></pre>
<h4 id="watcher">Watcher</h4>
<p><strong>useWatcher</strong> starts an <code>WebSocket</code> endpoint in the database that will send <em>watchData</em> when a change happens.</p>
<p><strong>WatchData</strong> is an object containing <em>model</em>, <em>event</em>, <em>data</em>.</p>
<ul>
<li><em>model</em>: The collection that were triggered</li>
<li><em>event</em>: The event that was triggered, either &#39;insert&#39;, &#39;update&#39; or &#39;delete&#39;</li>
<li><em>data</em>: List of documents that are related to the change</li>
</ul>
<p>To listen to these events on the client you have to create a WebSocket connection to <code>&#39;ws://&lt;hostname&gt;:&lt;port&gt;/watch-collections&#39;</code>.</p>
<pre><code class="lang-js"><span class="hljs-keyword">let</span> <span class="hljs-keyword">ws</span> = <span class="hljs-keyword">new</span> WebSocket(<span class="hljs-string">'ws://localhost:4000/watch-collections'</span>)
</code></pre>
<p>With the webSocket you can listen to messages from the collection channel.</p>
<pre><code class="lang-js">ws.onmessage = <span class="hljs-function"><span class="hljs-params">messageEvent</span> =&gt;</span> {
    <span class="hljs-keyword">const</span> watchData = <span class="hljs-built_in">JSON</span>.parse(messageEvent.data);

    <span class="hljs-comment">// deconstruct model, event and data from watchData</span>
    <span class="hljs-keyword">const</span> { model, event, data } = watchData;

    <span class="hljs-keyword">if</span>(model == <span class="hljs-string">'BlogPost'</span>) {
        <span class="hljs-comment">// do something with BlogPost</span>
    } 
    <span class="hljs-keyword">else</span> <span class="hljs-keyword">if</span>(model == <span class="hljs-string">'Message'</span>) {
        <span class="hljs-comment">// do something with Message</span>
    }
};
</code></pre>
<h4 id="example">Example</h4>
<p>Java:</p>
<pre><code class="lang-java">collection(<span class="hljs-built_in">option</span> -&gt; {
    <span class="hljs-built_in">option</span>.useWatcher = <span class="hljs-literal">true</span>;
});
</code></pre>
<p>JavaScript:</p>
<pre><code class="lang-js">ws.onmessage = <span class="hljs-function"><span class="hljs-params">messageEvent</span> =&gt;</span> {
    <span class="hljs-keyword">const</span> watchData = <span class="hljs-built_in">JSON</span>.parse(messageEvent.data);

    <span class="hljs-comment">// deconstruct model, event and data from watchData</span>
    <span class="hljs-keyword">const</span> { model, event, data } = watchData;

    <span class="hljs-keyword">switch</span>(event) {
        <span class="hljs-keyword">case</span> <span class="hljs-string">'insert'</span>:
            <span class="hljs-comment">// add post to list</span>
            model == <span class="hljs-string">'BlogPost'</span> &amp;&amp; posts.push(data[<span class="hljs-number">0</span>]);
            model == <span class="hljs-string">'Message'</span> &amp;&amp; <span class="hljs-comment">// add message to list</span>
        <span class="hljs-keyword">break</span>;
        <span class="hljs-keyword">case</span> <span class="hljs-string">'update'</span>:
        <span class="hljs-keyword">break</span>;
        <span class="hljs-keyword">case</span> <span class="hljs-string">'delete'</span>:
            <span class="hljs-comment">// remove post from list</span>
            model == <span class="hljs-string">'BlogPost'</span> &amp;&amp; (posts = posts.filter(<span class="hljs-function"><span class="hljs-params">post</span> =&gt;</span> post.id !== data[<span class="hljs-number">0</span>].id));
        <span class="hljs-keyword">break</span>;
    };

    <span class="hljs-comment">// update </span>
    renderPosts();
};
</code></pre>
<h4 id="browser">Browser</h4>
<p><strong>useBrowser</strong> will enable the collection browser. This lets you peek at the stored data while developing. 
It might be a good idea to disable this when deploying to save CPU and RAM.</p>
<pre><code class="lang-java">collection(<span class="hljs-built_in">option</span> -&gt; {
    <span class="hljs-built_in">option</span>.useBrowser = <span class="hljs-literal">true</span>; 
});
</code></pre>
<h2 id="document">Document</h2>
<p>Collections can be used as a simple key/value store, but it&#39;s true potential is when using it with POJOs. (Plain Old Java Objects)
When using POJOs the following two annotations must be present in the class.</p>
<h3 id="-document-annotation">@Document Annotation</h3>
<p>Marks a class to be used with a collection. Is required if an object is going to be saved to the collection.</p>
<h3 id="-id-annotation">@Id Annotation</h3>
<p>Each object in a Collection must be uniquely identified by a String field marked with <strong>@Id</strong> annotation. The collection maintains an unique index on that field to identify the objects.
If no id is manually set, the Collection will generate an UUID to that field when inserted or saved.</p>
<pre><code class="lang-java"><span class="hljs-meta"><span class="hljs-meta-keyword">import</span> nosqlite.annotations.Document;</span>
<span class="hljs-meta"><span class="hljs-meta-keyword">import</span> nosqlite.annotations.Id;</span>

@Document
<span class="hljs-keyword">public</span> <span class="hljs-class"><span class="hljs-keyword">class</span> <span class="hljs-title">MyType</span> </span>{

    @Id
    <span class="hljs-keyword">private</span> String id;
    <span class="hljs-keyword">private</span> String name;
}
</code></pre>
<h2 id="collection-methods">Collection methods</h2>
<p>To use the collection you need to add which document to query for in the collection parameter, ex <code>collection(&quot;User&quot;)</code> will only query for Users.
Data is stored in the collection as JSON, and the <code>find()</code>-methods parse this JSON to target class.
<code>findAsJson()</code>-methods is MUCH MUCH faster, because no parsing is required. This is good when only sending data from a collection directly over the network.</p>
<p><strong>Table 1. Collection methods</strong></p>
<table>
<thead>
<tr>
<th>Operation</th>
<th>Method</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr>
<td>Get all documents</td>
<td>find(Filter)</td>
<td>Returns a list with objects. If no filter is used find() will return ALL documents.</td>
</tr>
<tr>
<td>Get one document</td>
<td>findOne(Filter)</td>
<td>Returns first found document.</td>
</tr>
<tr>
<td>Get document with id</td>
<td>findById(id)</td>
<td>Returns the object with matching id.</td>
</tr>
<tr>
<td>Get all documents as JSON</td>
<td>findAsJson(Filter)</td>
<td>Returns a list with objects as JSON. If no filter is used find() will return ALL documents.</td>
</tr>
<tr>
<td>Get one document as JSON</td>
<td>findOneAsJson(Filter)</td>
<td>Returns first found document as JSON.</td>
</tr>
<tr>
<td>Get document with id as JSON</td>
<td>findByIdAsJson(id)</td>
<td>Returns the object with matching id as JSON.</td>
</tr>
<tr>
<td>Create or Update a document</td>
<td>save(Object)</td>
<td>Creates a new document in the collection if no id is present. If theres an id save() will update the existing document in the collection. Can save an array of documents.</td>
</tr>
<tr>
<td>Update documents</td>
<td>updateField(fieldName, newValue)</td>
<td>Update all documents fields with new value.</td>
</tr>
<tr>
<td>Update a document field with Object</td>
<td>updateField(Object, fieldName, newValue)</td>
<td>Updates the document field with matching id.</td>
</tr>
<tr>
<td>Update a document field with id</td>
<td>updateFieldById(id, fieldName, newValue)</td>
<td>Updates the document field with matching id.</td>
</tr>
<tr>
<td>Update documents</td>
<td>changeFieldName(newFieldName, oldFieldName)</td>
<td>Change field name on all documents.</td>
</tr>
<tr>
<td>Update documents</td>
<td>removeField(fieldName)</td>
<td>Removes field from all documents.</td>
</tr>
<tr>
<td>Delete a document</td>
<td>delete(Document)</td>
<td>Deletes the document with matching id.</td>
</tr>
<tr>
<td>Delete documents</td>
<td>delete(Filter)</td>
<td>Deletes all documents matching the filter.</td>
</tr>
<tr>
<td>Delete a document with id</td>
<td>deleteById(id)</td>
<td>Deletes the document with matching id.</td>
</tr>
<tr>
<td>Get number of documents</td>
<td>count()</td>
<td>Returns the count of all documents in a collection.</td>
</tr>
<tr>
<td>Watch a collection</td>
<td>watch(lambda)</td>
<td>Register a watcher that triggers on changes in the collection.</td>
</tr>
<tr>
<td>Watch a collection on an event</td>
<td>watch(event, lambda)</td>
<td>Register a watcher that triggers on changes at target event in the collection.</td>
</tr>
</tbody>
</table>
<p><strong>Table 1.2. Collection as a key/value store methods</strong></p>
<p>When using the collection as a key/value store you can name the collection anything you want.</p>
<pre><code class="lang-java">collection(<span class="hljs-string">"pets"</span>).<span class="hljs-built_in">put</span>(<span class="hljs-string">"snuggles"</span>, <span class="hljs-keyword">new</span> Cat(<span class="hljs-string">"Snuggles"</span>));
collection(<span class="hljs-string">"pets"</span>).<span class="hljs-built_in">put</span>(<span class="hljs-string">"pluto"</span>, <span class="hljs-keyword">new</span> Dog(<span class="hljs-string">"Pluto"</span>));

Dog pluto = collection(<span class="hljs-string">"pets"</span>).<span class="hljs-built_in">get</span>(<span class="hljs-string">"pluto"</span>, Dog.<span class="hljs-keyword">class</span>);
</code></pre>
<table>
<thead>
<tr>
<th>Operation</th>
<th>Method</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr>
<td>Get value by key</td>
<td>get(key)</td>
<td>Returns an object as JSON.</td>
</tr>
<tr>
<td>Get value by key as a POJO</td>
<td>get(key, class)</td>
<td>Returns an object parsed to target class.</td>
</tr>
<tr>
<td>Store object at key</td>
<td>put(key, value)</td>
<td>Stores the value as JSON at target key. Replaces value if key exists.</td>
</tr>
<tr>
<td>Store object at key</td>
<td>putIfAbsent(key, value)</td>
<td>Stores the value as JSON at target key. Does not replace value if key exists.</td>
</tr>
<tr>
<td>Remove value by key</td>
<td>remove(key)</td>
<td>Removes both key and value.</td>
</tr>
</tbody>
</table>
<h3 id="filters">Filters</h3>
<p>Filter are the selectors in the collection’s find operation. It matches documents in the collection depending on the criteria provided and returns a list of objects.</p>
<p>Make sure you import the static method <strong>Filter</strong>.</p>
<pre><code class="lang-java"><span class="hljs-meta"><span class="hljs-meta-keyword">import</span> static nosqlite.utilities.Filter.*;</span>
</code></pre>
<p><strong>Table 2. Comparison Filter</strong></p>
<table>
<thead>
<tr>
<th>Filter</th>
<th>Method</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr>
<td>Equals</td>
<td>eq(String, Object)</td>
<td>Matches values that are equal to a specified value.</td>
</tr>
<tr>
<td>NotEquals</td>
<td>ne(String, Object)</td>
<td>Matches values that are not equal to a specified value.</td>
</tr>
<tr>
<td>Greater</td>
<td>gt(String, Object)</td>
<td>Matches values that are greater than a specified value.</td>
</tr>
<tr>
<td>GreaterEquals</td>
<td>gte(String, Object)</td>
<td>Matches values that are greater than or equal to a specified value.</td>
</tr>
<tr>
<td>Lesser</td>
<td>lt(String, Object)</td>
<td>Matches values that are less than a specified value.</td>
</tr>
<tr>
<td>LesserEquals</td>
<td>lte(String, Object)</td>
<td>Matches values that are less than or equal to a specified value.</td>
</tr>
<tr>
<td>In</td>
<td>in(String, Object[])</td>
<td>Matches any of the values specified in an array.</td>
</tr>
</tbody>
</table>
<p><strong>Table 3. Logical Filters</strong></p>
<table>
<thead>
<tr>
<th>Filter</th>
<th>Method</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr>
<td>Not</td>
<td>not(Filter)</td>
<td>Inverts the effect of a filter and returns results that do not match the filter.</td>
</tr>
<tr>
<td>Or</td>
<td>or(Filter...)</td>
<td>Joins filters with a logical OR returns all ids of the documents that match the conditions of either filter.</td>
</tr>
<tr>
<td>And</td>
<td>and(Filter...)</td>
<td>Joins filters with a logical AND returns all ids of the documents that match the conditions of both filters.</td>
</tr>
</tbody>
</table>
<p><strong>Table 4. Text Filters</strong></p>
<table>
<thead>
<tr>
<th>Filter</th>
<th>Method</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr>
<td>Text</td>
<td>text(String, String)</td>
<td>Performs full-text search. Same rules as <a target="_blank" href="https://www.w3schools.com/sql/sql_like.asp">SQL LIKE</a></td>
</tr>
<tr>
<td>Regex</td>
<td>regex(String, String)</td>
<td>Selects documents where values match a specified regular expression.</td>
</tr>
</tbody>
</table>
<h3 id="findoptions">FindOptions</h3>
<p>A FindOptions is used to specify search options. It provides pagination as well as sorting mechanism.
The config syntax with lambda is more clear and easier to read.</p>
<p>Example</p>
<pre><code class="lang-java">// sorts all documents by age <span class="hljs-keyword">in</span> ascending order <span class="hljs-keyword">then</span> take <span class="hljs-built_in">first</span> <span class="hljs-number">10</span> documents <span class="hljs-keyword">and</span> <span class="hljs-built_in">return</span> as a List
List&lt;User&gt; users = collection(<span class="hljs-string">"User"</span>).find(null, <span class="hljs-string">"age=asc"</span>, <span class="hljs-number">10</span>, <span class="hljs-number">0</span>);

// <span class="hljs-keyword">or</span> with FindOptions
List&lt;User&gt; users = collection(<span class="hljs-string">"User"</span>).find(<span class="hljs-built_in">op</span> -&gt; {
        <span class="hljs-built_in">op</span>.<span class="hljs-built_in">sort</span> = <span class="hljs-string">"age=asc"</span>;
        <span class="hljs-built_in">op</span>.<span class="hljs-built_in">limit</span> = <span class="hljs-number">10</span>;
    });
</code></pre>
<pre><code class="lang-java">// sorts the documents by age <span class="hljs-keyword">in</span> ascending <span class="hljs-keyword">order</span>
<span class="hljs-title">List</span><span class="hljs-tag">&lt;User&gt;</span> users = collection(<span class="hljs-string">"User"</span>).find(null, <span class="hljs-string">"age=asc"</span>, <span class="hljs-number">0</span>, <span class="hljs-number">0</span>);

// <span class="hljs-keyword">or</span> with FindOptions
List<span class="hljs-tag">&lt;User&gt;</span> users = collection(<span class="hljs-string">"User"</span>).find(<span class="hljs-keyword">op</span> -&gt; {
        <span class="hljs-keyword">op</span>.filter = <span class="hljs-string">"age=asc"</span>;
    });
</code></pre>
<pre><code class="lang-java">// fetch <span class="hljs-number">10</span> documents starting from <span class="hljs-attr">offset</span> = <span class="hljs-number">2</span>
List&lt;User&gt; <span class="hljs-attr">users</span> = collection(<span class="hljs-string">"User"</span>).find(<span class="hljs-number">10</span>, <span class="hljs-number">2</span>);

// <span class="hljs-literal">or</span> <span class="hljs-keyword">with</span> FindOptions
List&lt;User&gt; <span class="hljs-attr">users</span> = collection(<span class="hljs-string">"User"</span>).find(op -&gt; {
        op.<span class="hljs-attr">limit</span> = <span class="hljs-number">10</span>;
        op.<span class="hljs-attr">offset</span> = <span class="hljs-number">2</span>;
    });
</code></pre>
<h2 id="collection-examples">Collection Examples</h2>
<p><strong>and()</strong></p>
<pre><code class="lang-java">// matches all documents where <span class="hljs-string">'age'</span> field has value as <span class="hljs-number">30</span> <span class="hljs-keyword">and</span>
// <span class="hljs-string">'name'</span> field has value as John Doe
collection(<span class="hljs-string">"User"</span>).find(<span class="hljs-keyword">and</span>(e<span class="hljs-string">q("age", 30)</span>, e<span class="hljs-string">q("name", "John Doe")</span>));
<span class="hljs-regexp">//</span> with the statement syntax
collection(<span class="hljs-string">"User"</span>).find(<span class="hljs-string">"age==30 &amp;&amp; name==John Doe"</span>);
</code></pre>
<p><strong>or()</strong></p>
<pre><code class="lang-java">// matches all documents where <span class="hljs-string">'age'</span> field has value as <span class="hljs-number">30</span> <span class="hljs-keyword">or</span>
// <span class="hljs-string">'name'</span> field has value as John Doe
collection(<span class="hljs-string">"User"</span>).find(<span class="hljs-keyword">or</span>(e<span class="hljs-string">q("age", 30)</span>, e<span class="hljs-string">q("name", "John Doe")</span>));
<span class="hljs-regexp">//</span> with the statement syntax
collection(<span class="hljs-string">"User"</span>).find(<span class="hljs-string">"age==30 || name==John Doe"</span>);
</code></pre>
<p><strong>not()</strong></p>
<pre><code class="lang-java">// matches all documents where <span class="hljs-string">'age'</span> field has value <span class="hljs-keyword">not</span> equals to <span class="hljs-number">30</span>
// <span class="hljs-keyword">and</span> name is <span class="hljs-keyword">not</span> John Doe
collection(<span class="hljs-string">"User"</span>).find(<span class="hljs-keyword">not</span>(<span class="hljs-keyword">and</span>((e<span class="hljs-string">q("age", 30)</span>, e<span class="hljs-string">q("name", "John Doe")</span>)));
<span class="hljs-regexp">//</span> with the statement syntax
collection(<span class="hljs-string">"User"</span>).find(<span class="hljs-string">"!(age==30 &amp;&amp; name==John Doe)"</span>);
</code></pre>
<p><strong>eq()</strong></p>
<pre><code class="lang-java"><span class="hljs-comment">// matches all documents where 'age' field has value as 30</span>
<span class="hljs-selector-tag">collection</span>(<span class="hljs-string">"User"</span>)<span class="hljs-selector-class">.find</span>(eq(<span class="hljs-string">"age"</span>, <span class="hljs-number">30</span>));
<span class="hljs-comment">// with the statement syntax</span>
<span class="hljs-selector-tag">collection</span>(<span class="hljs-string">"User"</span>)<span class="hljs-selector-class">.find</span>(<span class="hljs-string">"age==30"</span>);
</code></pre>
<p><strong>ne()</strong></p>
<pre><code class="lang-java"><span class="hljs-comment">// matches all documents where 'age' field has value not equals to 30</span>
<span class="hljs-selector-tag">collection</span>(<span class="hljs-string">"User"</span>)<span class="hljs-selector-class">.find</span>(ne(<span class="hljs-string">"age"</span>, <span class="hljs-number">30</span>));
<span class="hljs-comment">// with the statement syntax</span>
<span class="hljs-selector-tag">collection</span>(<span class="hljs-string">"User"</span>)<span class="hljs-selector-class">.find</span>(<span class="hljs-string">"age!=30"</span>);
</code></pre>
<p><strong>gt()</strong></p>
<pre><code class="lang-java"><span class="hljs-comment">// matches all documents where 'age' field has value greater than 30</span>
<span class="hljs-selector-tag">collection</span>(<span class="hljs-string">"User"</span>)<span class="hljs-selector-class">.find</span>(gt(<span class="hljs-string">"age"</span>, <span class="hljs-number">30</span>));
<span class="hljs-comment">// with the statement syntax</span>
<span class="hljs-selector-tag">collection</span>(<span class="hljs-string">"User"</span>)<span class="hljs-selector-class">.find</span>(<span class="hljs-string">"age&gt;30"</span>);
</code></pre>
<p><strong>gte()</strong></p>
<pre><code class="lang-java"><span class="hljs-comment">// matches all documents where 'age' field has value greater than or equal to 30</span>
<span class="hljs-selector-tag">collection</span>(<span class="hljs-string">"User"</span>)<span class="hljs-selector-class">.find</span>(gte(<span class="hljs-string">"age"</span>, <span class="hljs-number">30</span>));
<span class="hljs-comment">// with the statement syntax</span>
<span class="hljs-selector-tag">collection</span>(<span class="hljs-string">"User"</span>)<span class="hljs-selector-class">.find</span>(<span class="hljs-string">"age&gt;=30"</span>);
</code></pre>
<p><strong>lt()</strong></p>
<pre><code class="lang-java"><span class="hljs-comment">// matches all documents where 'age' field has value less than 30</span>
<span class="hljs-selector-tag">collection</span>(<span class="hljs-string">"User"</span>)<span class="hljs-selector-class">.find</span>(lt(<span class="hljs-string">"age"</span>, <span class="hljs-number">30</span>));
<span class="hljs-comment">// with the statement syntax</span>
<span class="hljs-selector-tag">collection</span>(<span class="hljs-string">"User"</span>)<span class="hljs-selector-class">.find</span>(<span class="hljs-string">"age&lt;30"</span>);
</code></pre>
<p><strong>lte()</strong></p>
<pre><code class="lang-java"><span class="hljs-comment">// matches all documents where 'age' field has value lesser than or equal to 30</span>
<span class="hljs-selector-tag">collection</span>(<span class="hljs-string">"User"</span>)<span class="hljs-selector-class">.find</span>(lte(<span class="hljs-string">"age"</span>, <span class="hljs-number">30</span>));
<span class="hljs-comment">// with the statement syntax</span>
<span class="hljs-selector-tag">collection</span>(<span class="hljs-string">"User"</span>)<span class="hljs-selector-class">.find</span>(<span class="hljs-string">"age&lt;=30"</span>);
</code></pre>
<p><strong>in()</strong></p>
<pre><code class="lang-java"><span class="hljs-comment">// matches all documents where 'age' field has value in [20, 30, 40]</span>
collection(<span class="hljs-string">"User"</span>).<span class="hljs-built_in">find</span>(<span class="hljs-built_in">in</span>(<span class="hljs-string">"age"</span>, <span class="hljs-number">20</span>, <span class="hljs-number">30</span>, <span class="hljs-number">40</span>));

<span class="hljs-built_in">List</span> ages = <span class="hljs-built_in">List</span>.of(<span class="hljs-number">20</span>, <span class="hljs-number">30</span>, <span class="hljs-number">40</span>);
collection(<span class="hljs-string">"User"</span>).<span class="hljs-built_in">find</span>(<span class="hljs-built_in">in</span>(<span class="hljs-string">"age"</span>, ages));

<span class="hljs-comment">// with the statement syntax</span>
collection(<span class="hljs-string">"User"</span>).<span class="hljs-built_in">find</span>(<span class="hljs-string">"age==[20, 30, 40]"</span>);
</code></pre>
<p><strong>text()</strong>
Same rules as <a target="_blank" href="https://www.w3schools.com/sql/sql_like.asp">SQL LIKE</a></p>
<ul>
<li>The percent sign (%) represents zero, one, or multiple characters</li>
<li>The underscore sign (_) represents one, single character</li>
</ul>
<pre><code class="lang-java">// matches <span class="hljs-keyword">all</span> documents where <span class="hljs-symbol">'address</span>' field start <span class="hljs-keyword">with</span> <span class="hljs-string">"a"</span>
collection(<span class="hljs-string">"User"</span>).find(<span class="hljs-literal">text</span>(<span class="hljs-string">"address"</span>, <span class="hljs-string">"a%"</span>));

// <span class="hljs-keyword">with</span> the statement syntax, applies <span class="hljs-keyword">to</span> <span class="hljs-keyword">all</span> <span class="hljs-literal">text</span>() examples
collection(<span class="hljs-string">"User"</span>).find(<span class="hljs-string">"address=~a%"</span>);

// matches <span class="hljs-keyword">all</span> documents where <span class="hljs-symbol">'address</span>' field <span class="hljs-keyword">end</span> <span class="hljs-keyword">with</span> <span class="hljs-string">"a"</span>
collection(<span class="hljs-string">"User"</span>).find(<span class="hljs-literal">text</span>(<span class="hljs-string">"address"</span>, <span class="hljs-string">"%a"</span>));

// matches <span class="hljs-keyword">all</span> documents where <span class="hljs-symbol">'address</span>' field have <span class="hljs-string">"or"</span> <span class="hljs-keyword">in</span> any position
collection(<span class="hljs-string">"User"</span>).find(<span class="hljs-literal">text</span>(<span class="hljs-string">"address"</span>, <span class="hljs-string">"%or%"</span>));

// matches <span class="hljs-keyword">all</span> documents where <span class="hljs-symbol">'address</span>' field have <span class="hljs-string">"r"</span> <span class="hljs-keyword">in</span> the second position
collection(<span class="hljs-string">"User"</span>).find(<span class="hljs-literal">text</span>(<span class="hljs-string">"address"</span>, <span class="hljs-string">"_r%"</span>));

// matches <span class="hljs-keyword">all</span> documents where <span class="hljs-symbol">'address</span>' field start <span class="hljs-keyword">with</span> <span class="hljs-string">"a"</span> <span class="hljs-keyword">and</span> are at least <span class="hljs-number">2</span> characters <span class="hljs-keyword">in</span> length
collection(<span class="hljs-string">"User"</span>).find(<span class="hljs-literal">text</span>(<span class="hljs-string">"address"</span>, <span class="hljs-string">"a_%"</span>));

// matches <span class="hljs-keyword">all</span> documents where <span class="hljs-symbol">'address</span>' field start <span class="hljs-keyword">with</span> <span class="hljs-string">"a"</span> <span class="hljs-keyword">and</span> are at least <span class="hljs-number">3</span> characters <span class="hljs-keyword">in</span> length
collection(<span class="hljs-string">"User"</span>).find(<span class="hljs-literal">text</span>(<span class="hljs-string">"address"</span>, <span class="hljs-string">"'a__%"</span>));

// matches <span class="hljs-keyword">all</span> documents where <span class="hljs-symbol">'address</span>' field start <span class="hljs-keyword">with</span> <span class="hljs-string">"a"</span> <span class="hljs-keyword">and</span> ends <span class="hljs-keyword">with</span> <span class="hljs-string">"o"</span>
collection(<span class="hljs-string">"User"</span>).find(<span class="hljs-literal">text</span>(<span class="hljs-string">"address"</span>, <span class="hljs-string">"a%o"</span>));
</code></pre>
<p><strong>regex()</strong></p>
<pre><code class="lang-java">// matches <span class="hljs-keyword">all</span> documents where <span class="hljs-symbol">'name</span>' value starts <span class="hljs-keyword">with</span> <span class="hljs-symbol">'jim</span>' <span class="hljs-keyword">or</span> <span class="hljs-symbol">'joe</span>'.
collection(<span class="hljs-string">"User"</span>).find(regex(<span class="hljs-string">"name"</span>, <span class="hljs-string">"^(jim|joe).*"</span>));
// <span class="hljs-keyword">with</span> the statement syntax
collection(<span class="hljs-string">"User"</span>).find(<span class="hljs-string">"name~~^(jim|joe).*"</span>);
</code></pre>
<h2 id="filter-nested-objects">Filter nested objects</h2>
<p>It&#39;s just as easy to filter nested objects in a collection. Each nested property is accessible with a dot-filter for each level.</p>
<pre><code class="lang-java"><span class="hljs-comment">// matches all documents where a User's cat has an age of 7</span>
<span class="hljs-selector-tag">collection</span>(<span class="hljs-string">"User"</span>)<span class="hljs-selector-class">.find</span>(eq(<span class="hljs-string">"cat.age"</span>, <span class="hljs-number">7</span>));
<span class="hljs-comment">// with the statement syntax</span>
<span class="hljs-selector-tag">collection</span>(<span class="hljs-string">"User"</span>)<span class="hljs-selector-class">.find</span>(<span class="hljs-string">"cat.age==7"</span>);

<span class="hljs-comment">// matches all documents where a User's headphone has a brand of Bose</span>
<span class="hljs-selector-tag">collection</span>(<span class="hljs-string">"User"</span>)<span class="hljs-selector-class">.find</span>(eq(<span class="hljs-string">"accessory.headphones.brand"</span>, <span class="hljs-string">"Bose"</span>));
<span class="hljs-comment">// with the statement syntax</span>
<span class="hljs-selector-tag">collection</span>(<span class="hljs-string">"User"</span>)<span class="hljs-selector-class">.find</span>(<span class="hljs-string">"accessory.headphones.brand==Bose"</span>);
</code></pre>
<h2 id="import">Import</h2>
<p>The collection supports mocking data as JSON, from example <a  target="_blank" href="https://www.mockaroo.com/">mockaroo.com</a>.
<strong>Note</strong>: Format must be a JSON-array.</p>
<p>Simply select a .json-file and click <code>import</code>. This will append the data to the collection.
It&#39;s important that the fields match the <strong>document</strong> in the collection.</p>
<h2 id="export">Export</h2>
<p>Export will download the current collection as a .json-file.</p>
<p>This file can easily be used to import into the collection, and can serve as a backup.</p>
<p>The .json-file is also created in the db-directory with the name of the document.</p>
<h2 id="drop">Drop</h2>
<p>Will delete all data in the collection.</p>
<h3 id="important-note-">Important note!</h3>
<p>Changing the name of a field will not corrupt the database, but will temporarily remove the value from all documents.
Simply revert the name and the value gets restored. 
You can manage fields with <a href="#collection-methods">collection methods</a>.</p>

        </div >
    `
}