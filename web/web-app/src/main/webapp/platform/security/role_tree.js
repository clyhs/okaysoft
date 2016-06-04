getTree: function(dataUrl,rootText,rootId,icon){
        var Tree = Ext.tree;
        var tree = new Tree.TreePanel({
            frame : true,// 美化界面
            split : true, //分隔条
            animate:true,
            autoScroll:true,
            collapsible : true,
            region:'west',
            width:200,
            loader: new Tree.TreeLoader({dataUrl:dataUrl+'?time='+new Date().toString()}),
            //loader: new Ext.tree.DWRTreeLoader({dataUrl: treeNodeManager.getTree}),
            containerScroll: false,
            border: false,
            rootVisible: rootId!="root"
            
        });

        // set the root node
        var root = new Tree.AsyncTreeNode({
            text: rootText,
            iconCls : icon,
            draggable:false, // disable root node dragging
            id:rootId
        });
        tree.setRootNode(root);
        tree.on('click',function(node, event){
            this.onClick(node, event);                    
        },this);
        //根节点装载完毕后，自动选择刚装载的节点
        
        root.reload(
            function(){
                root.expand(false, true);
                this.onClick(root.childNodes[0]);
            },
            this);
        return tree;
    },
    
    onClick: function(node, event){
    	this.select(node, event);
    },
    
    reloadTree : function() {
        this.tree.enable();
        this.tree.getLoader().dataUrl = this.storeURL;
        this.tree.getLoader().load(this.tree.root);
    },
    
    getTreeWithContextMenu: function(dataUrl, rootText, rootId, icon){            
        var tree = this.getTree(dataUrl, rootText, rootId,icon);                
        
        return tree;
    },
    
    
    select: function(node,event){
        node.expand(false, true);
        this.currentNode=node;
        this.currentId=node.id.split("-")[1];
        this.currentName=node.text;
        this.grid.setTitle("已选中【"+this.currentName+"】");
        
    },
    
    refreshTree: function(forceRefreshParentNode){
        if(this.currentNode.parentNode && forceRefreshParentNode){
            this.currentNode.parentNode.reload(
                // node loading is asynchronous, use a load listener or callback to handle results
                function(){
                    this.currentNode=this.tree.getNodeById(this.currentId);
                    this.select(this.currentNode);
                },
            this);
        }else{
            if(!this.currentNode.isExpandable()){
                //当前节点是叶子节点（新添加的节点是当前节点的第一个子节点）
                if(this.currentNode.parentNode==null){
                	this.tree.root.reload();
                	this.tree.root.expand(false, true);
                }else{
                    //重新加载当前节点的父节点，这样才能把新添加的节点装载进来
                    this.currentNode.parentNode.reload(
                        // node loading is asynchronous, use a load listener or callback to handle results
                        function(){
                            //重新查找当前节点，因为已经重新加载过数据
                            this.currentNode=this.tree.getNodeById(this.currentId);
                            //展开当前节点
                            this.currentNode.expand(false, true);
                        },
                    this);
                }
            }else{
                //重新加载当前节点
                this.currentNode.reload(
                    // node loading is asynchronous, use a load listener or callback to handle results
                    function(){
                        //展开当前节点
                        this.currentNode.expand(false, true);
                    },
                this);
            }
        }
    },